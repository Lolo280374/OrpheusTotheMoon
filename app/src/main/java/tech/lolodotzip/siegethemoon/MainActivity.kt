package tech.lolodotzip.siegethemoon

import android.content.Context
import android.content.SharedPreferences
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
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.lolodotzip.siegethemoon.ui.theme.OrpheusTotheMoonTheme

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("siege_prefs", Context.MODE_PRIVATE)

    fun getTotalMoon(): Int = prefs.getInt("total_moon", 0)
    fun setTotalMoon(count: Int) {
        prefs.edit().putInt("total_moon", count).apply()
    }
    fun incrementMoon() {
        val current = getTotalMoon()
        setTotalMoon(current + 1)
    }

    fun getTotalTime(): Long = prefs.getLong("total_time", 0L)
    fun setTotalTime(milliseconds: Long){
        prefs.edit().putLong("total_time", milliseconds).apply()
    }
    fun addTime(milliseconds: Long){
        val current = getTotalTime()
        setTotalTime(current + milliseconds)
    }

    fun getCoins(): Int = prefs.getInt("coins", 0)
    fun setCoins(amount: Int){
        prefs.edit().putInt("coins", amount).apply()
    }
    fun addCoins(amount: Int){
        val current = getCoins()
        setCoins(current + amount)
    }
    fun removeCoins(amount: Int){
        val current = getCoins()
        setCoins((current - amount).coerceAtLeast(0))
    }

    fun getCosmetic(): String = prefs.getString("selected_cosmetic", "orpheus") ?: "orpheus"
    fun setCosmetic(cosmetic: String){
        prefs.edit().putString("selected_cosmetic", cosmetic).apply()
    }
}

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
                1 -> fly(storeClicked = { currentTab = 3 })
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

        Text(
            text = "welcome!",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "let's help heidi and orpheus reach the moon!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

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
fun fly(storeClicked: () -> Unit = {}) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var totalDistance by remember { mutableStateOf(0f) }
    var hasReachedTop by remember { mutableStateOf(false) }
    var Initializing by remember { mutableStateOf(true) }
    var selectedCosmetic by remember { mutableStateOf(preferencesManager.getCosmetic()) }

    LaunchedEffect(Unit) {
        selectedCosmetic = preferencesManager.getCosmetic()
        delay(100)
        scrollState.scrollTo(scrollState.maxValue)
        delay(50)
        Initializing = false
    }

    LaunchedEffect(scrollState.value) {
        if (!Initializing && scrollState.value == 0 && !hasReachedTop) {
            hasReachedTop = true
            preferencesManager.incrementMoon()
            preferencesManager.addCoins(5)
            totalDistance = 0f
        } else if (scrollState.value > 0) {
            hasReachedTop = false
        }
    }

    DisposableEffect(Unit){
        val timeOnFly = System.currentTimeMillis()
        return@DisposableEffect onDispose {
            val timeSpent = System.currentTimeMillis() - timeOnFly
            preferencesManager.addTime(timeSpent)
        }
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

    val maxHeight = scrollState.maxValue.toFloat()
    val progress = if (maxHeight > 0){
        ((maxHeight - scrollState.value) / maxHeight * 100).toInt()
    } else {
        0
    }

    val cosmeticImage = when(selectedCosmetic){
        "cowboy" -> R.drawable.orpheuscowboyhat
        "sailor" -> R.drawable.orpheussailorhat
        "satchel" -> R.drawable.orpheussatchel
        else -> R.drawable.orpheus
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState, enabled = false)
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_full),
                contentDescription = "background",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12480.dp),
                contentScale = ContentScale.Crop
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.8f)
                .height(300.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ){
                Image(
                    painter = painterResource(id = cosmeticImage),
                    contentDescription = "orpheus flying on a PCB",
                    modifier = Modifier
                        .size(180.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (hasReachedTop){
                    Text(
                        text = "you reached the moon!",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    scrollState.scrollTo(scrollState.maxValue)
                                    hasReachedTop = false
                                }
                            },
                            modifier = Modifier
                                .padding(end = 16.dp)
                        ) {
                            Text("restart flight")
                        }
                        Button(
                            onClick = storeClicked,
                            modifier = Modifier
                                .padding(start = 16.dp)
                        ) {
                            Text("view store")
                        }
                    }
                } else {
                    Text(
                        text = "start shaking to fly to the moon!",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$progress% of the way there!",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }

}

@Composable
fun stats() {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    var totalMoon by remember { mutableStateOf(preferencesManager.getTotalMoon()) }
    var totalTime by remember { mutableStateOf(preferencesManager.getTotalTime()) }
    var coins by remember { mutableStateOf(preferencesManager.getCoins()) }

    LaunchedEffect(Unit) {
        totalMoon = preferencesManager.getTotalMoon()
        totalTime = preferencesManager.getTotalTime()
    }

    fun formatTime(milliseconds: Long): String{
        val totalSeconds = milliseconds / 1000
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02dmin %02ds", minutes, seconds)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "times you went to the moon",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                totalMoon.toString(),
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "total time flying",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                formatTime(totalTime),
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "current coins",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    coins.toString(),
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.siegecoin),
                    contentDescription = "coins represnted by the siege logo",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun store() {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    var coins by remember { mutableStateOf(preferencesManager.getCoins()) }
    var selectedCosmetic by remember { mutableStateOf(preferencesManager.getCosmetic()) }
    var cowboyState by remember { mutableStateOf("buy now!") }
    var sailorState by remember { mutableStateOf("buy now!") }
    var satchelState by remember { mutableStateOf("buy now!") }

    fun buyCosmetic(cosmeticName: String, cosmeticKey: String, price: Int, onButtonStateChange: (String) -> Unit){
        if (coins >= price){
            coins -= price
            preferencesManager.removeCoins(price)
            preferencesManager.setCosmetic(cosmeticKey)
            selectedCosmetic = cosmeticKey
            onButtonStateChange("owned!")
        } else {
            onButtonStateChange("not enough coins!!")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.orpheuscowboyhat),
                contentDescription = "cowboy hat cosmetic",
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "cowboy hat cosmetic",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                "10 coins",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    buyCosmetic("cowboy", "cowboy", 10) { newState ->
                        cowboyState = newState
                    }
                },
                modifier = Modifier
                    .padding(start = 16.dp)
            ){
                Text(cowboyState)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.orpheussailorhat),
                contentDescription = "sailor hat cosmetic",
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "sailor hat cosmetic",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                "20 coins",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    buyCosmetic("sailor", "sailor", 20) { newState ->
                        sailorState = newState
                    }
                },
                modifier = Modifier
                    .padding(start = 16.dp)
            ){
                Text(sailorState)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.orpheussatchel),
                contentDescription = "satchel cosmetic",
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "satchel cosmetic",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                "30 coins",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    buyCosmetic("satchel", "satchel", 30){ newState ->
                        satchelState = newState
                    }
                },
                modifier = Modifier
                    .padding(start = 16.dp)
            ){
                Text(satchelState)
            }
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    OrpheusTotheMoonTheme {
        SiegeTheMoonApp()
    }
}
