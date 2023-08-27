package com.example.testtaskintership.presentation.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.testtaskintership.data.api.ApiService
import com.example.testtaskintership.domain.model.Data
import com.example.testtaskintership.domain.model.MainDataModel
import com.example.testtaskintership.domain.model.SecondaryDataModel
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
            Tab(
                text = { Text("Камеры") },
                selected = pagerState.currentPage == 0,
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(0) } }
            )
            Tab(
                text = { Text("Двери") },
                selected = pagerState.currentPage == 1,
                onClick = { scope.launch { pagerState.animateScrollToPage(1) } }
            )
        }
        HorizontalPager(state = pagerState) { page ->
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
@Composable
fun DoorsListScreen(items: State<SecondaryDataModel>) {
    LazyColumn {
        items(items.value.data) { item ->
            Text(text = item.name)
        }
    }
}
@Composable
fun CamerasListScreen(items: State<MainDataModel>) {
    LazyColumn {
        items(items.value.data.cameras) { item ->
            Text(text = item.name)
        }
    }
}
