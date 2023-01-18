package uz.qmgroup.pharmadealers.features.core.ui

import android.content.res.Configuration
import android.icu.text.NumberFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ImportInProgressScreen(
    modifier: Modifier,
    progresses: Map<String, Float>
) {
    LazyColumn(modifier = modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items(progresses.entries.toList()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = it.key,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(5f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                LinearProgressIndicator(
                    progress = it.value,
                    modifier = Modifier.weight(7f)
                )

                Text(
                    text = NumberFormat.getPercentInstance().format(it.value),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1.5f)
                )
            }
        }
    }
}

@Preview(name = "Import completed", showSystemUi = true, showBackground = true)
@Preview(
    name = "Import completed dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true, showBackground = true
)
@Composable
fun ImportInProgressScreenPreview() {
    ImportInProgressScreen(
        modifier = Modifier.fillMaxSize(), progresses = mapOf(
            "OOO Pharm Gate" to 0f,
            "OOO Aero Pharm " to 0.2f,
            "OOO Muhtasar Pharm " to 0.5f,
            "OOO MixPlusMed Pharm " to 0.75f,
            "OOO Aliyabana Pharm " to 1.00f,
        )
    )
}