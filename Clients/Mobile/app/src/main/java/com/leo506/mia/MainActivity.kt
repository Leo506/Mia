package com.leo506.mia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import viewModels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityView()
        }
    }
}

@Composable
fun MainActivityView(
    viewModel: MainViewModel = MainViewModel()
) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = viewModel::refresh) {
        ArticlesList()
    }
}

@Composable
@Preview
fun ArticlesList() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        items(5) {
            Article()
        }
    }
}

@Composable
@Preview
fun Article(
    title: String = "Title",
    author: String = "u/leo506",
    uri: String = "https://www.reddit.com/r/programming/comments/155nvnp/what_does_a_cto_actually_do/"

) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                context.startActivity(intent);
            },
        shape = RoundedCornerShape(50)
    ) {
        Column(
            modifier = Modifier
                .background(Color.Gray)
                .padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
        ) {
            Text(text = title, fontSize = 25.sp, color = Color.White, fontStyle = FontStyle.Italic)
            Text(text = author, fontSize = 15.sp, color = Color.Black)
        }
    }
}