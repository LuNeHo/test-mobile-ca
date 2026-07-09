package com.luneho.testmobileca.presentation.operations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.luneho.testmobileca.domain.model.Operation
import com.luneho.testmobileca.presentation.utils.formatAsMoney
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs
import kotlin.time.Instant


@Composable
fun OperationsScreen(
    accountLabel: String,
    operations: List<Operation>,
    viewModel: OperationsViewModel = koinViewModel(
        parameters = { parametersOf(operations, accountLabel) }
    ),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { OperationTopBar(state.accountLabel, onBack) },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { padding ->
        OperationsContent(padding, state)
    }
}

@Composable
private fun OperationsContent(padding: PaddingValues, state: OperationsState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        when {
            state.operations.isEmpty() -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("No operations found.")
            }

            else -> {
                val totalAmount = state.operations.sumOf { abs(it.amount) }
                Text(
                    text = totalAmount.formatAsMoney(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 42.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                ) {
                    itemsIndexed(
                        state.operations,
                        key = { index, operation -> "${operation.id}_$index" }) { _, operation ->
                        OperationRow(operation)
                    }
                }
            }
        }
    }
}

@Composable
private fun OperationTopBar(accountLabel: String, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(accountLabel) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
        }
    )
}

@Composable
private fun OperationRow(operation: Operation) {
    Column(modifier = Modifier.background(Color.White)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 40.dp, top = 12.dp, bottom = 12.dp),
        ) {
            Text(operation.title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = abs(operation.amount).formatAsMoney(),
                color = MaterialTheme.colorScheme.secondaryFixedDim,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Text(
            modifier = Modifier.padding(start = 40.dp, end = 12.dp, bottom = 12.dp),
            text = operation.date.formatAsDate(),
            style = MaterialTheme.typography.titleSmall
        )
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
    }
}

private fun Long.formatAsDate(): String {
    val date = Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
    val day = date.day.toString().padStart(2, '0')
    val month = date.date.month.number.toString().padStart(2, '0')
    return "$day/$month/${date.year}"
}

@Preview
@Composable
fun OperationsScreenPreview() {
    val accountLabel = "Compte de dépôt"
    val operations = listOf(
        Operation("2", "Prélèvement Netflix", -15.99, 1644870724),
        Operation("4", "CB Amazon", -95.99, 1644611558)
    )
    val state = OperationsState(accountLabel, operations)

    MaterialTheme {
        OperationsContent(PaddingValues(), state)
    }
}