package com.example.blinknotes.speedTest

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.blinknotes.R

@Composable
fun HiFPTDetailSpeedTestScreen (){
    val viewModel: HiFPTDetailSpeedTestScreenViewModel = viewModel(factory = HiFPTDetailSpeedTestScreenViewModelFactory())
    val uiState by viewModel.uiState.collectAsState()
    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = colorResource(id = R.color.lightgray)
            ),
        topBar = {
            TopBarDetailSpeedTest()

        }
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
                .background(
                    color = colorResource(id = R.color.lightgray)
                )
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            InfoInternet(
                wifiName = uiState.wifiName ,
                idContract = uiState.idContract ,
                supplierName = uiState.supplierName ,
                ipAddress = uiState.ipAddress ,
                deviceName = uiState.deviceName ,
                testDate = uiState.testDate,)
            ResultInternet(
                ping = uiState.ping,
                jitter = uiState.jitter,
                downloadSpeed = uiState.downloadSpeed,
                uploadSpeed = uiState.uploadSpeed)

        }
    }

}
@Composable
fun InfoInternet(
    wifiName: String,
    idContract : String,
    supplierName: String,
    ipAddress: String,
    deviceName: String,
    testDate: String
){
    Column (
        modifier = Modifier

    ) {
        Text(
            text = "Thông tin Internet",
            modifier = Modifier,
            fontSize = 18.sp,

        )
        Column (
            modifier = Modifier
                .padding(top = 12.dp)
              //  .border(8.dp, Color.Black, RoundedCornerShape(8.dp))
                .background(
                    color = colorResource(R.color.white),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Row (
                modifier = Modifier
                    .background(
                        color = colorResource(R.color.white),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_wifi),
                    contentDescription = ""
                )
                Text(
                    text = wifiName,
                    modifier = Modifier
                )
            }
            Divider(
                color = colorResource(id = R.color.lightblue),
                thickness = 0.5.dp
            )
            Row  (
                modifier = Modifier
                    .background(
                        color = colorResource(R.color.white),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ){
                Text(
                    text = "Hợp đồng kiểm tra",
                    modifier = Modifier
                )
                Row  (
                    modifier = Modifier
                ) {
                    Text(
                        text = idContract,
                        modifier = Modifier
                    )
                    Image(
                        painter = painterResource(R.drawable.icon_wifi),
                        contentDescription = ""
                    )
                }
            }
            Inforow(
                title = "Nhà cung cấp",
                value = supplierName ,
                )
            Inforow(
                title = "Địa chỉ IP",
                value = ipAddress ,
                )
            Inforow(
                title = "Thiết bị",
                value = deviceName ,
                )
            Inforow(
                title = "Ngày kiểm tra",
                value = testDate ,
               )



        }
    }
}
@Composable
fun Inforow(
    title : String,
    value : String){
    Row  (
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(R.color.white),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {

        Text(
            text = title,
            fontSize = 16.sp ,
            fontWeight = FontWeight.Medium,
            color = colorResource(R.color.aliceblue),
            modifier = Modifier
        )
        Text(
            text = value,
            modifier = Modifier
        )
    }
}
@Composable
fun ResultInternet(
    ping: String,
    jitter: String,
    downloadSpeed: String,
    uploadSpeed: String){
    Column (
        modifier = Modifier
            .padding(top = 12.dp)
            //  .border(8.dp, Color.Black, RoundedCornerShape(8.dp))
            .background(
                color = colorResource(R.color.white),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)

    ) {
        Text(
            text = "Kết quả đo",

            )
        Row (
            modifier = Modifier
        ){
            ItemsResultInternet(
                title = "Ping",
                iconRes = R.drawable.icon_ping,
                value = ping)
            ItemsResultInternet(
                title = "Jitter",
                iconRes = R.drawable.icon_jitter,
                value = jitter)
        }
        Row (
            modifier = Modifier
        ){
            ItemsResultInternet(
                title = "Tải xuống",
                iconRes = R.drawable.icon_download,
                value = downloadSpeed)
            ItemsResultInternet(
                title = "Tải lên",
                iconRes = R.drawable.icon_upload,
                value = uploadSpeed)
        }
    }
}
@Composable
fun ItemsResultInternet(
    title: String,
    @DrawableRes iconRes: Int,
    value: String
){
    Column (
        modifier = Modifier
    ){
        Row (
            modifier = Modifier
        ){
            Text(
                text = title,

                )
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null
            )
        }
        Text(
            text = value,

            )
    }
}
@Composable
fun TopBarDetailSpeedTest(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(56.dp)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.icon_back),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

        }
        Text(
            text = "Kiểm tra tốc độ",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun P(){
    HiFPTDetailSpeedTestScreen()
}