package com.example.fitnesscentarchat.ui.screens.membership.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesscentarchat.data.models.MembershipModel
import com.example.fitnesscentarchat.data.models.User
import com.example.fitnesscentarchat.ui.screens.hub.components.getExpiryStatus

@Composable
fun TopTextSection(user: User?, membership: MembershipModel?, onTopTextPositioned: (Float) -> Unit) {
    val newUsername = user?.username ?: "-"
    val expires = getExpiryStatus(membership?.membershipDeadline) ?: "-.-.----"


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                Box(
                    modifier = Modifier.border(
                        1.dp,
                        Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(8.dp)
                    )
                        .background(color= Color(0x6B191919), shape = RoundedCornerShape(8.dp)
                        ),
                ){
                    Text("MEMBERSHIP", color= Color(0xFFFFFFFF),modifier= Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
                Text("$newUsername", color= Color(0xFFFFFFFF),modifier= Modifier.padding(8.dp), fontSize=20.sp)
                Text("${expires}", color= Color(0xFFFFFFFF),modifier= Modifier.padding(8.dp), fontSize=16.sp)
            }
        }


    }


}