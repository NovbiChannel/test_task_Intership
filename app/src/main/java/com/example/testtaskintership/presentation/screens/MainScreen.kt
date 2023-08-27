package com.example.testtaskintership.presentation.screens

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeableState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.testtaskintership.R
import com.example.testtaskintership.data.api.ApiService
import com.example.testtaskintership.domain.model.Data
import com.example.testtaskintership.domain.model.DataX
import com.example.testtaskintership.domain.model.MainDataModel
import com.example.testtaskintership.domain.model.SecondaryDataModel
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val service = ApiService.create()
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        2
    }
    val getDoors = produceState(
        initialValue = SecondaryDataModel(emptyList(), false),
        producer = {
            value = service.getDoors()
        }
    )
    val getCameras = produceState(
        initialValue = MainDataModel(Data(emptyList(), emptyList()), false),
        producer = {
            value = service.getCameras()
        }
    )
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
            contentColor = Color.Black
        ) {
            Tab(modifier = Modifier
                    .background(MaterialTheme.colors.onPrimary),
                text = { Text("Камеры") },
                selected = pagerState.currentPage == 0,
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(0) } }
            )
            Tab(
                modifier = Modifier
                    .background(MaterialTheme.colors.onPrimary),
                text = { Text("Двери") },
                selected = pagerState.currentPage == 1,
                onClick = { scope.launch { pagerState.animateScrollToPage(1) } }
            )
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                }
            }
        ) { page ->
            when (page) {
                0 -> {
                    CamerasListScreen(items = getCameras)
                }
                1 -> {
                    DoorsListScreen(items = getDoors)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DoorsListScreen(items: State<SecondaryDataModel>) {
    // Swipeable state
    val swipeableState = rememberSwipeableState(initialValue = false)
    val anchors = with(LocalDensity.current) {
        mapOf(0f to false, -128.dp.toPx() to true)
    }
    val scope = rememberCoroutineScope()
    val cornerRadius = 20.dp

    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(items.value.data) { item ->
            Box(
                modifier = Modifier
                    .swipeable(
                        state = swipeableState,
                        anchors = anchors,
                        thresholds = { _, _ -> FractionalThreshold(0.5f) },
                        orientation = Orientation.Horizontal,
                    )
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(cornerRadius))
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(cornerRadius))
                    .background(MaterialTheme.colors.surface)
            ) {
                DoorItem(item = item, swipeableState = swipeableState, scope = scope)
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DoorItem(item: DataX, swipeableState: SwipeableState<Boolean>, scope: CoroutineScope) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.fillMaxWidth()) {
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

        Text(
            text = item.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(top = 8.dp, start = 8.dp)
                .align(Alignment.Start)
        )
    }

    if (swipeableState.currentValue) {
        val animationSpec: AnimationSpec<Float> = TweenSpec()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(16.dp)
                .clip(MaterialTheme.shapes.medium)
                .clickable {
                    scope.launch {
                        swipeableState.animateTo(
                            targetValue = false,
                            anim = animationSpec
                        )
                    }
                },
            contentAlignment = Alignment.CenterEnd
        ) {
            val iconId = if (item.favorites) R.drawable.ic_fav_true_24dp else R.drawable.ic_fav_false_24dp
            val favIcon = painterResource(id = iconId)
            FloatingActionButton(
                onClick = { /* Toggle favorites */ },
                modifier = Modifier.size(48.dp),
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = Color.White
            ) {
                Image(
                    painter = favIcon,
                    contentDescription = "favorites icon"
                )
            }
        }
    }
}
@Composable
fun CamerasListScreen(items: State<MainDataModel>) {
    val groupedCameras = items.value.data.cameras.groupBy { it.room }

    LazyColumn {
        this.items(items.value.data.room) { room ->
            Text(
                text = room,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            )

            groupedCameras[room]?.let { cameras ->
                cameras.forEach { camera ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.onPrimary)
                            .padding(16.dp)
                            .clip(MaterialTheme.shapes.medium),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                        ) {
                            val painter = rememberCoilPainter(
                                request = camera.snapshot,
                                fadeIn = true,
                            )
                            Image(
                                painter = painter,
                                contentDescription = "camera snapshot",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxSize()
                            )

                            if (camera.rec) {
                                val recIcon = painterResource(id = R.drawable.ic_rec_24dp)
                                Image(
                                    painter = recIcon,
                                    contentDescription = "recording icon",
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(8.dp)
                                )
                            }

                            if (camera.favorites) {
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

                        Text(
                            text = camera.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(top = 8.dp, start = 8.dp)
                                .align(Alignment.Start)
                        )
                    }
                }
            }
        }
    }
}