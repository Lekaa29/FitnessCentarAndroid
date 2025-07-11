package com.example.fitnesscentarchat.ui.screens.fitnessCenter.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fitnesscentarchat.data.models.Article
import com.example.fitnesscentarchat.data.models.Coach
import com.example.fitnesscentarchat.data.models.Program
import com.example.fitnesscentarchat.ui.screens.fitnessCenter.FitnessCenterViewModel


@Composable
fun SingleCoachView(
    programs: List<Program>,
    onChatClicked: (Int, Int, String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: FitnessCenterViewModel,
    coachUserId: Int,
    coachName: String
) {

    var conversationId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(coachUserId) { // will reload if coach changes
        conversationId = viewModel.getConversationIdByRecipient(coachUserId)
    }



    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // Top Bar with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to articles",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        transparentButton(text = "Contact", onClick = { onChatClicked(conversationId ?: -1,coachUserId, coachName) })
        // Scrollable content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(programs) { program ->
                ProgramItemRow(
                    name = program.title ?: "",
                    price = String.format("%.2f", program.price),
                    durationWeeks = program.weekDuration.toString() ?: "0",
                    description = program.description ?: "no description",
                    onClick = { }
                )
            }
        }
    }
}




@Composable
fun SingleArticleView(
    article: Article,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Top Bar with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to articles",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Scrollable content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                // Article Image
                AsyncImage(
                    model = article.coverPictureLink ?: "",
                    contentDescription = article.coverPictureLink,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Article Title
                Text(
                    text = article.title ?: "",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Article Content (placeholder)
                Text(
                    text = article.text ?: "",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
        }
    }
}


@Composable
fun CoachesItemRow(
    name: String,
    price: String,
    imageUrl: String?,
    programCount: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(284.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF010101)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Image on the left (half width)
            AsyncImage(
                model = imageUrl,
                contentDescription = price,
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight()
                    .background(Color.Transparent),
                contentScale = ContentScale.Crop
            )


            // Text on the right (half width)
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight(),
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(top=8.dp)
                ){
                    Column {
                        Text(
                            text = name,
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "$programCount programs",
                            color = Color.White,
                            fontSize = 16.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Text(
                    text = "$description",
                    color = Color.White,
                    lineHeight = 14.sp,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    maxLines = 7,
                    modifier = Modifier.padding(top=30.dp),
                    overflow = TextOverflow.Visible
                )
            }
        }
    }
}



@Composable
fun ProgramItemRow(
    name: String,
    price: String,
    durationWeeks: String,
    description: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.95f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFF1D1D1D),
                            Color(0xFF252525),
                        )
                    )
                ),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight()
                    .padding(16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                ){
                    Column {
                        Box(
                        ){
                            Text(
                                text = name,
                                color = Color.White,
                                fontSize = 36.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center,
                                modifier= Modifier
                                    .padding(top = 20.dp)
                                    .fillMaxWidth()
                                    .height(56.dp)

                            )
                        }
                        Row(
                            modifier = Modifier.padding(top=4.dp)
                        ){
                            Text(
                                text = "Duration:     $durationWeeks",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "weeks",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.padding(start = 6.dp),
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(){
                            Text(
                                text = "Price:          $price",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Eur/hr",
                                color = Color.White,
                                fontWeight = FontWeight.Light,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(start = 6.dp),

                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Row {
                            Text(
                                text = "Description:",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 14.sp,
                                fontSize = 16.sp,
                                maxLines = 3,
                                modifier = Modifier.padding(top=8.dp),
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = description,
                                color = Color.White,
                                lineHeight = 14.sp,
                                fontSize = 16.sp,
                                maxLines = 6, // Increase from 3 to 6 or higher
                                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                                overflow = TextOverflow.Ellipsis
                            )

                        }

                    }
                }

            }
        }
    }
}






@Composable
fun CoachesOverlay(
    coaches: List<Coach>,
    onChatClicked: (Int, Int, String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: FitnessCenterViewModel
) {
    var selectedArticle by remember { mutableStateOf<Coach?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Create a blurred background effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    // Draw a stronger semi-transparent overlay
                    drawRect(
                        color = Color(0xFF000000), // 80% opacity black
                        size = size
                    )
                }
        )

        // Content with glassmorphism effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xF0000000),
                            Color(0xF0000000),
                        )
                    )
                )
        ) {
            if (selectedArticle != null) {

                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    SingleCoachView(
                        programs = selectedArticle!!.programs,
                        onBackClick = { selectedArticle = null },
                        onChatClicked = onChatClicked,
                        coachUserId = selectedArticle?.user?.id ?: -1,
                        coachName = "${selectedArticle?.user?.firstName} ${selectedArticle?.user?.lastName}",
                        viewModel = viewModel
                        )
                }

            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Top Bar with Back Button and Title
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Text(
                            text = "Coaches & Programs",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.size(48.dp))
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(coaches) { coach ->
                            CoachesItemRow(
                                name = "${coach.user.firstName} ${coach.user.lastName}" ?: "",
                                price = coach.programs.firstOrNull()?.price.toString() ?: "0.0",
                                programCount = coach.programs.count().toString(),
                                imageUrl = coach.bannerPictureLink,
                                description = coach.description,
                                onClick = { selectedArticle = coach }
                            )
                        }
                    }
                }
            }
        }
    }
}














