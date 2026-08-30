package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ExpenseCategory
import com.example.data.model.ExpenseEntity
import com.example.data.model.ReceiptItem
import com.example.data.model.TripEntity
import com.example.data.sample.SampleData
import com.example.ui.components.SampleReceiptsBottomSheet
import com.example.ui.viewmodel.TripViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddExpenseScreen(
    trip: TripEntity,
    viewModel: TripViewModel,
    expenseToEdit: ExpenseEntity? = null,
    onNavigateBack: () -> Unit
) {
    val isScanning by viewModel.isReceiptScanning.collectAsStateWithLifecycle()
    val parsedReceipt by viewModel.parsedReceipt.collectAsStateWithLifecycle()

    var title by remember { mutableStateOf(expenseToEdit?.title ?: "") }
    var amountStr by remember { mutableStateOf(expenseToEdit?.amount?.let { if (it > 0) it.toString() else "" } ?: "") }
    var selectedCategory by remember {
        mutableStateOf(
            if (expenseToEdit != null) expenseToEdit.expenseCategory else ExpenseCategory.FOOD_DINING
        )
    }
    var paymentMethod by remember { mutableStateOf(expenseToEdit?.paymentMethod ?: "Credit Card") }
    var location by remember { mutableStateOf(expenseToEdit?.location ?: "") }
    var notes by remember { mutableStateOf(expenseToEdit?.notes ?: "") }
    var tagsStr by remember { mutableStateOf(expenseToEdit?.tags ?: "") }
    var isAiParsed by remember { mutableStateOf(expenseToEdit?.isAiParsed ?: false) }

    // Group Bill Split State
    var paidBy by remember { mutableStateOf(expenseToEdit?.paidBy?.ifBlank { "You" } ?: "You") }
    var splitType by remember { mutableStateOf(expenseToEdit?.splitType ?: "EQUAL") }
    val splitMembersState = remember {
        mutableStateListOf<String>().apply {
            val initial = expenseToEdit?.getSplitMembers(trip.membersList) ?: trip.membersList
            addAll(initial.ifEmpty { listOf("You") })
        }
    }

    // Itemized receipt list
    val itemsList = remember { mutableStateListOf<ReceiptItem>() }

    // Dialog state for sample receipts
    var showSamplePicker by remember { mutableStateOf(false) }
    var showRawTextDialog by remember { mutableStateOf(false) }
    var rawReceiptInputText by remember { mutableStateOf("") }

    // Gallery Image Picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.processImageUri(uri)
        }
    }

    // Reactively update form when Gemini finishes parsing a receipt
    LaunchedEffect(parsedReceipt) {
        parsedReceipt?.let { parsed ->
            if (parsed.merchant.isNotBlank()) title = parsed.merchant
            if (parsed.totalAmount > 0) amountStr = String.format(Locale.US, "%.2f", parsed.totalAmount)
            selectedCategory = ExpenseCategory.fromString(parsed.category)
            if (parsed.paymentMethod.isNotBlank()) paymentMethod = parsed.paymentMethod
            if (parsed.summaryNote.isNotBlank()) notes = parsed.summaryNote
            if (parsed.tags.isNotEmpty()) tagsStr = parsed.tags.joinToString(", ")
            isAiParsed = true

            itemsList.clear()
            itemsList.addAll(parsed.items)
        }
    }

    // Populate initial items from editing expense if present
    LaunchedEffect(expenseToEdit) {
        if (expenseToEdit != null && expenseToEdit.itemsJson.isNotBlank()) {
            try {
                val array = JSONArray(expenseToEdit.itemsJson)
                itemsList.clear()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    itemsList.add(
                        ReceiptItem(
                            name = obj.optString("name", "Item"),
                            quantity = obj.optInt("quantity", 1),
                            price = obj.optDouble("price", 0.0)
                        )
                    )
                }
            } catch (_: Exception) {}
        }
    }

    // Pulsing shimmer animation for Gemini AI Scanning
    val infiniteTransition = rememberInfiniteTransition(label = "gemini_scan_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (expenseToEdit != null) "Edit Expense" else "Add Trip Expense",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.clearParsedReceipt()
                            onNavigateBack()
                        },
                        modifier = Modifier.testTag("btn_back_expense")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {

            // Top Hero Banner: Google Gemini Receipt Auto-Scanner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("gemini_scan_banner"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E1B4B)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF312E81),
                                    Color(0xFF1E1B4B),
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF59E0B).copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Gemini AI",
                                    tint = Color(0xFFFBBF24),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Google Gemini Receipt Scanner",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Auto-extracts merchant, amount, items & category",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (isScanning) {
                            // Scanning Progress State
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White.copy(alpha = 0.12f),
                                modifier = Modifier.fillMaxWidth().scale(pulseScale)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(22.dp),
                                        color = Color(0xFF38BDF8),
                                        strokeWidth = 2.5.dp
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Gemini is analyzing receipt & categorizing...",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF38BDF8)
                                    )
                                }
                            }
                        } else {
                            // 3 Quick Action Trigger Buttons: Pick Image, Paste Text, Test Sample
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF4F46E5),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { imagePickerLauncher.launch("image/*") }
                                        .testTag("btn_scan_image")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Image,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Upload Image",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White.copy(alpha = 0.18f),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { showRawTextDialog = true }
                                        .testTag("btn_paste_receipt")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Description,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Paste Text",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFF59E0B),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { showSamplePicker = true }
                                        .testTag("btn_sample_receipts")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Try Sample",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // AI categorization notification banner if parsed
            if (isAiParsed) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF10B981).copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Auto-categorized as ${selectedCategory.displayName} via Gemini",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF059669)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Expense Form Fields
            Text(
                text = "Expense Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Merchant / Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Merchant / Description") },
                leadingIcon = { Icon(Icons.Default.Store, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_expense_title"),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Amount & Location
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("Total Amount") },
                    leadingIcon = {
                        Text(
                            trip.currencySymbol,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(start = 14.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_expense_amount"),
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("City / Spot") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_expense_location"),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Category Selection with visual icons
            Text(
                text = "Category",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ExpenseCategory.entries.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        leadingIcon = {
                            Icon(
                                imageVector = cat.icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else cat.color,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        label = { Text(cat.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = cat.color,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Payment Method Chips
            Text(
                text = "Payment Method",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            val paymentMethods = listOf("Credit Card", "Cash", "Debit Card", "Mobile Pay", "Other")
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                paymentMethods.forEach { method ->
                    val isSelected = paymentMethod == method
                    FilterChip(
                        selected = isSelected,
                        onClick = { paymentMethod = method },
                        label = { Text(method, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Itemized Breakdown from Receipt
            if (itemsList.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Itemized Receipt Breakdown (${itemsList.size})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            IconButton(
                                onClick = {
                                    itemsList.add(ReceiptItem(name = "New Item", quantity = 1, price = 0.0))
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Item",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        itemsList.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${item.quantity}x ${item.name}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "${trip.currencySymbol}${String.format(Locale.US, "%.2f", item.price)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                IconButton(
                                    onClick = { itemsList.removeAt(index) },
                                    modifier = Modifier.size(24.dp).padding(start = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 👥 Group Bill Split Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Group Bill Split",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        val parsedAmt = amountStr.toDoubleOrNull() ?: 0.0
                        if (parsedAmt > 0 && splitMembersState.isNotEmpty() && splitType != "YOU_ONLY") {
                            val perPerson = parsedAmt / splitMembersState.size
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "${trip.currencySymbol}${String.format(Locale.US, "%.2f", perPerson)} / person",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 1. Who paid?
                    Text(
                        text = "Paid by:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        trip.membersList.forEach { member ->
                            FilterChip(
                                selected = paidBy.equals(member, ignoreCase = true),
                                onClick = { paidBy = member },
                                label = { Text(member, fontSize = 12.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 2. Split Mode
                    Text(
                        text = "Split method:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = splitType == "EQUAL",
                            onClick = {
                                splitType = "EQUAL"
                                if (splitMembersState.isEmpty()) splitMembersState.addAll(trip.membersList)
                            },
                            label = { Text("Split Equally", fontSize = 12.sp) }
                        )
                        FilterChip(
                            selected = splitType == "YOU_ONLY",
                            onClick = {
                                splitType = "YOU_ONLY"
                            },
                            label = { Text("Personal Only", fontSize = 12.sp) }
                        )
                    }

                    if (splitType != "YOU_ONLY") {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Split with (${splitMembersState.size} people):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            trip.membersList.forEach { member ->
                                val isChecked = splitMembersState.contains(member)
                                FilterChip(
                                    selected = isChecked,
                                    onClick = {
                                        if (isChecked) {
                                            if (splitMembersState.size > 1) {
                                                splitMembersState.remove(member)
                                            }
                                        } else {
                                            splitMembersState.add(member)
                                        }
                                    },
                                    label = { Text(member, fontSize = 12.sp) }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Tags
            OutlinedTextField(
                value = tagsStr,
                onValueChange = { tagsStr = it },
                label = { Text("Tags (comma separated, e.g. Dinner, Kyoto, Group)") },
                leadingIcon = { Icon(Icons.Default.Tag, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_expense_tags"),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes / Story") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_expense_notes"),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Save Expense Button
            Button(
                onClick = {
                    val amount = amountStr.toDoubleOrNull() ?: 0.0

                    // Convert itemsList to JSON string
                    val itemsJsonStr = if (itemsList.isNotEmpty()) {
                        val jsonArr = JSONArray()
                        itemsList.forEach { item ->
                            val obj = JSONObject()
                            obj.put("name", item.name)
                            obj.put("quantity", item.quantity)
                            obj.put("price", item.price)
                            jsonArr.put(obj)
                        }
                        jsonArr.toString()
                    } else ""

                    val splitWithStr = if (splitType == "YOU_ONLY") {
                        paidBy
                    } else {
                        splitMembersState.joinToString(", ")
                    }

                    if (expenseToEdit != null) {
                        viewModel.updateExpense(
                            expenseToEdit.copy(
                                title = title.ifBlank { "Trip Expense" },
                                amount = amount,
                                category = selectedCategory.displayName,
                                paymentMethod = paymentMethod,
                                notes = notes,
                                isAiParsed = isAiParsed,
                                tags = tagsStr,
                                itemsJson = itemsJsonStr,
                                location = location,
                                paidBy = paidBy,
                                splitType = splitType,
                                splitWith = splitWithStr
                            )
                        )
                    } else {
                        viewModel.addExpense(
                            tripId = trip.id,
                            title = title.ifBlank { "Trip Expense" },
                            amount = amount,
                            category = selectedCategory.displayName,
                            paymentMethod = paymentMethod,
                            notes = notes,
                            isAiParsed = isAiParsed,
                            tags = tagsStr,
                            itemsJson = itemsJsonStr,
                            location = location,
                            paidBy = paidBy,
                            splitType = splitType,
                            splitWith = splitWithStr
                        )
                    }

                    viewModel.clearParsedReceipt()
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("btn_save_expense"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (expenseToEdit != null) "Update Expense" else "Save to ${trip.name}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Sample Receipt Bottom Sheet
    if (showSamplePicker) {
        SampleReceiptsBottomSheet(
            onSelectPreset = { preset ->
                viewModel.loadSampleReceipt(preset)
            },
            onDismiss = { showSamplePicker = false }
        )
    }

    // Raw Receipt Text Input Dialog
    if (showRawTextDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showRawTextDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Paste Receipt Text")
                }
            },
            text = {
                Column {
                    Text(
                        text = "Paste text from email receipts, invoices, or SMS messages. Gemini will auto-extract total, items, and category:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = rawReceiptInputText,
                        onValueChange = { rawReceiptInputText = it },
                        placeholder = { Text("e.g. Starbucks Kyoto 1x Matcha Latte $6.50 Total: $6.50 Paid Visa") },
                        minLines = 4,
                        maxLines = 8,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (rawReceiptInputText.isNotBlank()) {
                            viewModel.scanReceiptWithGemini(
                                receiptText = rawReceiptInputText,
                                userHint = "User pasted raw receipt text"
                            )
                        }
                        showRawTextDialog = false
                    }
                ) {
                    Text("Scan with Gemini")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showRawTextDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
