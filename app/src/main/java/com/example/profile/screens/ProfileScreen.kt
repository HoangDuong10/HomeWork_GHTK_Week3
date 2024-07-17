package com.example.profile.screens
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.profile.R
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profile.viewmodel.ProfileViewModelProviderFactory
import com.example.profile.model.DataResponse
import com.example.profile.model.History
import com.example.profile.repository.ProfileRepository
import com.example.profile.util.Resource
import com.example.profile.viewmodel.ProfileViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.random.Random




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(){

    val profileRepository = ProfileRepository()
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelProviderFactory(profileRepository))

    LaunchedEffect(Unit) {
        println("LaunchedEffect: Calling getProfile()")
        viewModel.getProfile()
    }
    val profile by viewModel.profile.observeAsState()
    println("Profile state: $profile")
    when (profile) {
        is Resource.Loading -> {
            println("Profile is Loading")
            LoadingBar()
        }
        is Resource.Success -> {
            val mProfile = (profile as Resource.Success).data
            println("Profile is Success with data: $mProfile")
            Column {
                if (mProfile != null) {
                    Header(mProfile)
                }
                ListWalletFunction()
                TabLayout( mProfile?.listHistory)
            }

        }
        is Resource.Error -> {
            val message = (profile as Resource.Error).message
            println("Profile is Error with message: $message")
            Text(text = "Error: $message")
        }

        else -> {println("Profile state is unknown")}
    }


}

@Composable
fun Header(dataResponse: DataResponse){
    ConstraintLayout(modifier = Modifier.fillMaxWidth()){
        val(tvAccount,rowSetting,divider1,divider2,divider3,rowWallet,
            tvName,tvPosition,tvEmployeeProfile,ivAvata,ivNext,
            tvWallet) = createRefs()
        Text(text = stringResource(id = R.string.account),
            fontSize = getXnormalTextSize(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(getPaddingScreen())
                .constrainAs(tvAccount) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(rowSetting.bottom)
                }
            ,
        )

        Row(modifier = Modifier
            .padding(top = getPaddingScreen(), end = getPaddingScreen())
            .constrainAs(rowSetting) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }
            .border(
                border = BorderStroke(1.dp, color = colorResource(id = R.color.grey)),
                RoundedCornerShape(20.dp)
            )
            .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Icon(imageVector = Icons.Filled.Settings,
                contentDescription = null,
                Modifier.size(16.dp)

            )

            Text(text = stringResource(id = R.string.setting),
                fontSize = getSmallTextSize(),
                modifier = Modifier.padding(start = 2.dp)
            )
        }
        HorizontalDivider(Modifier.constrainAs(divider1){
            top.linkTo(rowSetting.bottom)
            start.linkTo(parent.start)
        })

        Image(painter = painterResource(id = R.drawable.ic_avata),
            contentDescription = null,
            Modifier
                .padding(getPaddingScreen())
                .constrainAs(ivAvata) {
                    start.linkTo(parent.start)
                    top.linkTo(divider1.bottom)
                }
                .size(100.dp)
                .clip(CircleShape)

        )

        dataResponse.fullName?.let {
            Text(text = it,
                fontSize = getXnormalTextSize(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = getPaddingScreen())
                    .constrainAs(tvName) {
                        start.linkTo(ivAvata.end)
                        top.linkTo(divider1.bottom)
                    }
                    .padding(top = getPaddingBetweenView()),
            )
        }

        dataResponse.position?.let {
            Text(text = it,
                fontSize = getNormalTextSize(),
                color = Color.Gray,
                modifier = Modifier
                    .padding(top = getPaddingBetweenView())
                    .constrainAs(tvPosition) {
                        start.linkTo(ivAvata.end)
                        top.linkTo(tvName.bottom)
                    }
            )
        }

        HorizontalDivider(Modifier.constrainAs(divider2){
            top.linkTo(tvPosition.bottom)
            start.linkTo(ivAvata.end)
        })

        Text(text = stringResource(id = R.string.employe_profile),
            fontSize = getNormalTextSize(),
            modifier = Modifier
                .padding(top = getPaddingBetweenView())
                .constrainAs(tvEmployeeProfile) {
                    top.linkTo(divider2.bottom)
                    start.linkTo(ivAvata.end)
                }
        )

        Icon(painter = painterResource(id = R.drawable.ic_next),
            contentDescription = null,
            modifier = Modifier
                .padding(top = getPaddingBetweenView())
                .constrainAs(ivNext) {
                    end.linkTo(parent.end)
                    top.linkTo(tvEmployeeProfile.top)
                    bottom.linkTo(tvEmployeeProfile.bottom)
                },
            tint = Color.Gray
        )

        HorizontalDivider(Modifier.constrainAs(divider3){
            top.linkTo(ivAvata.bottom)
            start.linkTo(parent.start)
        })

        Text(text = stringResource(id = R.string.wallet_GHTK),
            fontSize = getNormalTextSize(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(getPaddingScreen())
                .constrainAs(tvWallet) {
                    top.linkTo(divider3.bottom)
                    start.linkTo(parent.start)
                }
        )

        Row(modifier = Modifier
            .padding(top = getPaddingScreen())
            .constrainAs(rowWallet) {
                end.linkTo(parent.end)
                top.linkTo(divider3.bottom)
            }) {
            val walletVisibilityState = remember { mutableStateOf(false) }
            val isWalletValueVisible = walletVisibilityState.value
            val setWalletValueVisible = { newValue: Boolean ->
                walletVisibilityState.value = newValue
            }
            Icon(imageVector  = if (isWalletValueVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.clickable { setWalletValueVisible(!isWalletValueVisible) })
            Text(text = if (isWalletValueVisible) "123456" else "******",
                fontSize = getNormalTextSize(),
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null,)
        }
    }
}

@Composable
fun ItemWalletFunction(image: Int, content: Int){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(colorResource(id = R.color.grey), shape = RoundedCornerShape(12.dp))
        ,
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            alignment = Alignment.Center,
            modifier = Modifier
                .size(30.dp)
                .padding(getPaddingBetweenView()),
        )

        Text(
            text = stringResource(id = content),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                getPaddingBetweenView(),
            ),
        )
//
    }
}
@Composable
fun ListWalletFunction(){
    Row( modifier = Modifier
        .fillMaxWidth()
        .padding(start = getPaddingScreen(), end = getPaddingScreen()),
        horizontalArrangement = Arrangement.SpaceAround){
        ItemWalletFunction(image = R.drawable.ic_load_data, content = R.string.load_data)
        ItemWalletFunction(image = R.drawable.ic_prepaid_data, content = R.string.prepaid_data)
        ItemWalletFunction(image = R.drawable.ic_insurance, content = R.string.insurance)
        ItemWalletFunction(image = R.drawable.ic_electric_bill, content = R.string.electric_bill)

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout(listHistory: List<History>?) {
    val tabItems = listOf("Hành trình", "Hoạt động")
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState {
        tabItems.size
    }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)

    }


    Column {
        Box(
            modifier = Modifier
                .padding(top = getPaddingScreen(), bottom = getPaddingScreen())
                .fillMaxWidth()
                .height(1.dp)
                .background(color = colorResource(id = R.color.grey))
        )

        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            divider = {},
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(2.dp)
                        .padding(horizontal = 16.dp)
                        .background(color = colorResource(id = R.color.green))
                )
            },
        ) {
            tabItems.forEachIndexed { index, tabItem ->
                val select = pagerState.currentPage == index
                Tab(
                    selected = selectedTabIndex==index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(text = tabItem,
                            color = if (select) colorResource(id = R.color.green) else Color.Gray
                        )
                    },

                    )
            }
        }
        HorizontalPager(state = pagerState, userScrollEnabled = false) { page ->
            when (page) {
                0 -> listHistory?.let { TabContent(it) }

            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TabContent(listJourney: List<History>){
   Column {
       Row (modifier = Modifier
           .fillMaxWidth()
           .padding(
               top = getPaddingScreen(),
               bottom = getPaddingScreen(),
               start = getPaddingScreen()
           )
           .height(IntrinsicSize.Min),
           horizontalArrangement = Arrangement.SpaceBetween,
           verticalAlignment = Alignment.CenterVertically){
           ItemTabContent(value = "16", title = "Ngày")
           Box(modifier = Modifier
               .padding(vertical = 15.dp)
               .fillMaxHeight()
               .width(0.3.dp)
               .background(color = Color.Gray)

           ) {

           }
           ItemTabContent(value = "---", title = "Đơn hàng")
           Box(modifier = Modifier
               .padding(vertical = 15.dp)
               .fillMaxHeight()
               .width(0.3.dp)
               .background(color = Color.Gray)

           ) {

           }
           ItemTabContent(value = "---", title = "Shop & khách")
           Box(modifier = Modifier
               .fillMaxHeight()
               .width(0.3.dp)
               .background(color = Color.Gray)

           ) {

           }
           ItemTabContent(value = "---", title = "Đánh giá")
           Icon(painter = painterResource(id = R.drawable.ic_next),
               contentDescription = null,
           )
       }
       ItemJourney(listJourney =listJourney )
    }
}

@Composable
fun ItemTabContent(value : String,title : String){
    Column {
        Text(text = value,
            fontSize = getNormalTextSize(),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(start = getPaddingScreen()),
        )

        Text(
            text = title,
            fontSize = getSmallTextSize(),
            color = Color.Gray,
            modifier = Modifier.padding(top = getPaddingBetweenView(), start = getPaddingScreen()),
        )


    }
}

enum class DividerOrientation {
    Horizontal,
    Vertical,
}

@Composable
fun CustomDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    orientation: DividerOrientation = DividerOrientation.Horizontal,
    dashGap: Dp = 5.dp,
    dashLength: Dp = 5.dp,
    dashThickness: Dp = 3.dp,
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = dashThickness.toPx()
            val gap = dashGap.toPx()
            val length = dashLength.toPx()

            val paint =
                Paint().apply {
                    this.color = color
                    style = PaintingStyle.Stroke
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(length, gap), 0f)
                }
            if (orientation == DividerOrientation.Horizontal) {
                val centerY = size.height / 2
                drawLine(
                    color = color,
                    start = Offset(0f, centerY),
                    end = Offset(size.width, centerY),
                    strokeWidth = strokeWidth,
                    pathEffect = paint.pathEffect,
                )
            } else {
                val centerX = size.width / 2
                drawLine(
                    color = color,
                    start = Offset(centerX, 0f),
                    end = Offset(centerX, size.height),
                    strokeWidth = strokeWidth,
                    pathEffect = paint.pathEffect,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemJourney(history : History){
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (ivUpPosition, tvTitle, tvTime, divider) = createRefs()

        Icon(
            painter = if(history.isUp == true) painterResource(id = R.drawable.ic_up_position) else painterResource(
                id = R.drawable.ic_down_position
            ),
            contentDescription = null,
            tint = if(history.isUp == true) colorResource(id = R.color.green) else Color.Red,
            modifier =
            Modifier
                .padding()
                .size(20.dp)
                .constrainAs(ivUpPosition) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },
        )
        Text(
            text =
            buildAnnotatedString {
                withStyle(
                    style =
                    SpanStyle(
                        color = if(history.isUp == true) colorResource(id = R.color.green) else Color.Red,
                                fontSize = getNormalTextSize(),
                                fontWeight = FontWeight.Bold,
                    ),
                ) {
                    append(if(history.isUp == true) "UP! " else "DOWN! ")
                }
                withStyle(style = SpanStyle(colorResource(id = R.color.black))) {
                    append(history.title)
                }
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier =
            Modifier
                .padding(bottom = 20.dp)
                .constrainAs(tvTitle) {
                    start.linkTo(ivUpPosition.end)
                    top.linkTo(parent.top)
                    end.linkTo(tvTime.start)
                    width = Dimension.fillToConstraints
                },
        )
        Text(
            text = getRandomFormattedDate(),
            fontSize = 12.sp,
            color = Color.Gray,
            modifier =
            Modifier
                .padding(4.dp)
                .constrainAs(tvTime) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
        )


        CustomDivider(
            color = Color.Gray,
            orientation = DividerOrientation.Vertical,
            dashGap = 2.dp,
            dashLength = 6.dp,
            dashThickness = 1.dp,
            modifier =
            Modifier
                .constrainAs(divider) {
                    start.linkTo(parent.start)
                    top.linkTo(ivUpPosition.bottom)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .width(20.dp),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getRandomFormattedDate(): String {
    val startDate = LocalDate.of(2020, 1, 1)
    val endDate = LocalDate.of(2024, 8, 30)
    val daysBetween = ChronoUnit.DAYS.between(startDate, endDate)
    val randomDays = Random.nextLong(daysBetween + 1) // +1 để bao gồm endDate
    val randomDate = startDate.plusDays(randomDays)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return randomDate.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ItemJourney(listJourney : List<History>) {

    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(listJourney) { item ->
            ItemJourney(history = item)
        }
    }
}


@Composable
fun LoadingBar() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(color = colorResource(id = R.color.green))
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewItemWallet(){

    ProfileScreen()
}

@Composable
fun getNormalTextSize() : TextUnit {
    return dimensionResource(id = R.dimen.text_size_normal).value.sp
}

@Composable
fun getXnormalTextSize() : TextUnit {
    return dimensionResource(id = R.dimen.text_size_xnormal).value.sp
}

@Composable
fun getSmallTextSize() : TextUnit {
    return dimensionResource(id = R.dimen.text_size_small).value.sp
}

@Composable
fun getPaddingScreen() : Dp {
    return dimensionResource(id = R.dimen.define_dimen_8)
}

@Composable
fun getPaddingBetweenView() : Dp {
    return dimensionResource(id = R.dimen.define_dimen_4)
}

@Composable
fun HorizontalDivider(modifier: Modifier){
    Box(modifier = modifier
        .padding(top = 8.dp)
        .fillMaxWidth()
        .height(1.dp)
        .background(color = colorResource(id = R.color.grey)))
}