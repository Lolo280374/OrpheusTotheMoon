package tech.lolodotzip.siegethemoon

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.packInts
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.lolodotzip.siegethemoon.ui.theme.OrpheusTotheMoonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OrpheusTotheMoonTheme {
                SiegeTheMoonApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiegeTheMoonApp() {
    var currentTab by remember { mutableStateOf(0) }

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingToolbar(
                    currentTab = currentTab,
                    onTabSelected = { currentTab = it }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> home()
                1 -> fly()
                2 -> stats()
                3 -> store()
            }
        }
    }
}

@Composable
fun FloatingToolbar(currentTab: Int, onTabSelected: (Int) -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        tonalElevation = 6.dp,
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Row(
            modifier = Modifier
                .height(64.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { onTabSelected(0) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = if (currentTab == 0)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(Icons.Default.Home, contentDescription = "Home")
            }

            FilledIconButton(
                onClick = { onTabSelected(1) },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = if (currentTab == 1)
                        MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Icon(Icons.Default.Flight, contentDescription = "Fly!")
            }

            IconButton(
                onClick = { onTabSelected(2) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = if (currentTab == 2)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(Icons.Default.Equalizer, contentDescription = "Statistics")
            }

            IconButton(
                onClick = { onTabSelected(3) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = if (currentTab == 3)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(Icons.Default.Store, contentDescription = "Store")
            }
        }
    }
}

@Composable
fun home() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AndroidView(
            factory = {
                VideoView(it).apply {
                    setVideoURI(Uri.parse("android.resource://${context.packageName}/${R.raw.tutorial}"))
                    val mediaController = MediaController(it)
                    mediaController.setAnchorView(this)
                    setMediaController(mediaController)
                    setOnCompletionListener { start() }
                    start()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "watch the tutorial! (youtube: todonintendo)",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "are you done?",
            style = MaterialTheme.typography.labelLarge
        )

        Image(
            painter = painterResource(id = R.drawable.postutorial),
            contentDescription = "post tutorial help",
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun fly() {
    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var totalDistance by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        delay(100)
        scrollState.scrollTo(scrollState.maxValue)
    }

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            private var lastY = 0f
            private var smoothedVelocity = 0f

            override fun onSensorChanged(event: SensorEvent) {
                val y = event.values[1]
                val deltaY = y - lastY
                if (kotlin.math.abs(deltaY) > 2.0f) {
                    val impulse = kotlin.math.abs(deltaY) * 0.1f
                    smoothedVelocity += impulse
                }

                smoothedVelocity *= 0.93f

                if (smoothedVelocity > 0.05f) {
                    totalDistance += smoothedVelocity * 0.002f
                    coroutineScope.launch {
                        val newPos = (scrollState.value - smoothedVelocity * 5)
                            .coerceAtLeast(0f)
                        scrollState.scrollTo(newPos.toInt())
                    }
                }
                lastY = y
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_full),
                contentDescription = "background",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12480.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(80.dp))
        }

        Image(
            painter = painterResource(id = R.drawable.orpheus),
            contentDescription = "orpheus flying on a PCB",
            modifier = Modifier
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun stats() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("stats page tbd", style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun store() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("store page tbd", style = MaterialTheme.typography.headlineSmall)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    OrpheusTotheMoonTheme {
        SiegeTheMoonApp()
    }
}
