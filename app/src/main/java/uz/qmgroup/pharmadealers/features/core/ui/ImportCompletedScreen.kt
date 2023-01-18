package uz.qmgroup.pharmadealers.features.core.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.qmgroup.pharmadealers.R


@Composable
fun ImportCompletedScreen(
    modifier: Modifier,
    total: Int,
    onReady: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(0.2f),
                painter = painterResource(id = R.drawable.baseline_check_circle_24),
                contentDescription = "Success",
                contentScale = ContentScale.FillWidth
            )
            Text(text = "Import completed !", style = MaterialTheme.typography.labelLarge)
            Text(
                text = "Imported $total medicines",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
        Button(onClick = onReady, Modifier.align(Alignment.BottomEnd)) {
            Text(text = stringResource(R.string.Ready))
        }
    }
}

@Preview(name = "Import completed", showSystemUi = true, showBackground = true)
@Preview(name = "Import completed dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true, showBackground = true
)
@Composable
fun ImportCompletedScreenPreview() {
    ImportCompletedScreen(modifier = Modifier.fillMaxSize(), total = 1150, onReady = {})
}