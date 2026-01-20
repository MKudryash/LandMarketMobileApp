package com.example.landmarketmobileapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.landmarketmobileapp.R

@Composable
fun ContactDialog(
    phoneNumber: String,
    sellerName: String,
    onDismiss: () -> Unit,
    onCall: () -> Unit,
    onMessage: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.Companion.padding(24.dp),
                horizontalAlignment = Alignment.Companion.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Phone,
                    contentDescription = "Телефон",
                    tint = Color(0xFF6AA26C),
                    modifier = Modifier.Companion.size(48.dp)
                )

                Spacer(modifier = Modifier.Companion.height(16.dp))

                Text(
                    text = "Связаться с продавцом",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = sellerName,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.Companion.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.Companion.height(16.dp))

                Text(
                    text = phoneNumber,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = Color(0xFF6AA26C),
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.Companion.height(24.dp))

                Column(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            onCall()
                            onDismiss()
                        },
                        modifier = Modifier.Companion.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6AA26C)
                        ),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.Companion.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Companion.CenterVertically
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Позвонить")
                            Spacer(modifier = Modifier.Companion.width(12.dp))
                            Text("Позвонить")
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            onMessage()
                            onDismiss()
                        },
                        modifier = Modifier.Companion.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF6AA26C)
                        ),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.Companion.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Companion.CenterVertically
                        ) {
                            Icon(
                                painterResource(R.drawable.message),
                                contentDescription = "Написать"
                            )
                            Spacer(modifier = Modifier.Companion.width(12.dp))
                            Text("Написать сообщение")
                        }
                    }

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.Companion.fillMaxWidth()
                    ) {
                        Text("Отмена")
                    }
                }
            }
        }
    }
}