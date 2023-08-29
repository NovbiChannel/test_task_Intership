package com.example.testtaskintership.presentation.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testtaskintership.R
import com.example.testtaskintership.data.api.ApiService
import com.example.testtaskintership.domain.model.Camera
import com.example.testtaskintership.domain.model.Data
import com.example.testtaskintership.domain.model.DataX
import com.example.testtaskintership.domain.model.MainDataModel
import com.example.testtaskintership.domain.model.SecondaryDataModel
import com.example.testtaskintership.presentation.viewmodels.MainViewModel
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        2
    }
    val viewModel = remember { MainViewModel() }
    val scope = rememberCoroutineScope()
    Column {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Мой дом",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(25.dp))
        }
        TabRow(
            pagerState.currentPage,
            contentColor = MaterialTheme.colors.secondary,
        ) {
            Tab(modifier = Modifier
                    .background(MaterialTheme.colors.background),
                text = { Text("Камеры") },
                selectedContentColor = Color.Black,
                selected = pagerState.currentPage == 0,
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(0) } }
            )
            Tab(
                modifier = Modifier
                    .background(MaterialTheme.colors.background),
                text = { Text("Двери") },
                selectedContentColor = Color.Black,
                selected = pagerState.currentPage == 1,
                onClick = { scope.launch { pagerState.animateScrollToPage(1) } }
            )
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                }
            }
        ) { page ->
            var refreshState = false
            viewModel.isRefreshing.observeForever {
                refreshState = it
            }
            when (page) {
                0 -> {
                    val cameraState = viewModel.cameras.observeAsState()
                    SwipeRefreshWrapper(refreshState, { viewModel.onRefresh() }) {
                        CamerasListScreen(items = cameraState)
                    }
                }
                1 -> {
                    val doorsState = viewModel.doors.observeAsState()
                    SwipeRefreshWrapper(refreshState, { viewModel.onRefresh() }) {
                        DoorsListScreen(items = doorsState)
                    }
                }
            }
        }
    }
}
@Composable
fun DoorsListScreen(items: State<SecondaryDataModel?>) {
    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(items = items.value?.data ?: listOf()) { item ->
            Box(modifier = Modifier.fillMaxWidth()) {
                DoorItem(item = item)
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DoorItem(
    item: DataX
) {
    val cornerRadius = 16
    val swipeableState = rememberSwipeableState(false)
    val anchors = mapOf(0f to false, -150f to true)
    //Main Box
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .swipeable(
            state = swipeableState,
            anchors = anchors,
            thresholds = { _, _ -> FractionalThreshold(0f) },
            orientation = Orientation.Horizontal
        )
    ) {
        val offsetX = swipeableState.offset.value.dp

        // Animate padding and clip
        val padding = animateDpAsState(targetValue = if (swipeableState.targetValue) 0.dp else 16.dp).value
        val clipValue = animateIntAsState(targetValue = if (swipeableState.targetValue) 0 else cornerRadius).value.dp
        val clipCorner = RoundedCornerShape(clipValue)
        //Swipeable box
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = padding, vertical = 8.dp)
            .clip(clipCorner)
            .offset(x = offsetX)
            .zIndex(1f)
            .background(MaterialTheme.colors.primary)) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val painter = rememberCoilPainter(
                        request = item.snapshot,
                        fadeIn = true,
                    )
                    Image(
                        painter = painter,
                        contentDescription = "camera snapshot",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 32.dp, bottom = 32.dp)
                            .align(Alignment.CenterStart)
                    )

                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 16.dp)
                            .align(Alignment.CenterEnd)
                    ) {
                        val icon = painterResource(id = R.drawable.ic_lock_24dp)
                        Image(
                            painter = icon,
                            contentDescription = "lock icon"
                        )
                    }
                }
            }
        }
        //Iteration button box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.Center)
                .zIndex(0f)
        ) {
            Row(
                Modifier
                    .align(Alignment.CenterEnd)
            ) {
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterVertically)
                        .border(1.dp, MaterialTheme.colors.secondary, CircleShape)
                ) {
                    val editIcon = painterResource(id = R.drawable.ic_edit_24dp)
                    Image(
                        painter = editIcon,
                        contentDescription = "edit icon"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterVertically)
                        .border(1.dp, MaterialTheme.colors.secondary, CircleShape)
                ) {
                    val icon = if (item.favorites) R.drawable.ic_fav_true_24dp else R.drawable.ic_fav_false_24dp
                    val favoriteIcon = painterResource(id = icon)
                    Image(
                        painter = favoriteIcon,
                        contentDescription = "favorite icon"
                    )
                }
            }
        }
    }
}
@Composable
fun CamerasListScreen(items: State<MainDataModel?>) {
    val cornerRadius = 16.dp
    val camerasData = items.value?.data ?: Data(listOf(), listOf())
    val groupedCameras = camerasData.cameras.groupBy { it.room }

    LazyColumn {

        this.items(camerasData.room) { room ->
            // Проверяем, есть ли камеры в комнате
            if (groupedCameras[room]?.isNotEmpty() == true) {
                // Показываем текст с названием комнаты
                Text(
                    text = room,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }
            groupedCameras[room]?.let { cameras ->
                cameras.forEach { camera ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(cornerRadius))
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(cornerRadius))
                            .background(MaterialTheme.colors.primary)
                    ) {
                        CameraItem(item = camera)
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
@Composable
fun CameraItem(item: Camera) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            val painter = rememberCoilPainter(
                request = item.snapshot,
                fadeIn = true,
            )
            Image(
                painter = painter,
                contentDescription = "camera snapshot",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxSize()
            )

            if (item.rec) {
                val recIcon = painterResource(id = R.drawable.ic_rec_24dp)
                Image(
                    painter = recIcon,
                    contentDescription = "recording icon",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                )
            }

            if (item.favorites) {
                val favIcon = painterResource(id = R.drawable.ic_fav_true_24dp)
                Image(
                    painter = favIcon,
                    contentDescription = "favorites icon",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }
        }
        Box(modifier =
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
        ) {
            Text(
                text = item.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(start = 16.dp, top = 32.dp, bottom = 32.dp)
                    .align(Alignment.CenterStart)
            )
        }
    }
}
@Composable
fun SwipeRefreshWrapper(
    refreshState: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshState),
        onRefresh = onRefresh,
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                scale = true,
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.secondary
            )
        }
    ) {
        content()
    }
}

@Preview
@Composable
fun DoorImagePreview() {
    val service = ApiService.create()
    val getDoors = produceState(
        initialValue = SecondaryDataModel(emptyList(), false),
        producer = {
            value = service.getDoors()
        }
    )
}

@Preview
@Composable
fun CamerasImagePreview() {
    val service = ApiService.create()
    val getCameras = produceState(
        initialValue = MainDataModel(Data(emptyList(), emptyList()), false), 
        producer = {
            value = service.getCameras()
        }
    )
    CamerasListScreen(items = getCameras)
}