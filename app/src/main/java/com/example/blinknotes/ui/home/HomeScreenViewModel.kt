package com.example.blinknotes.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


private val allImages = listOf(
    "https://i.pinimg.com/736x/42/a1/06/42a1068eb0bcdb65da6e60bf49af66fe.jpg",
    "https://internetviettel.vn/wp-content/uploads/2017/05/H%C3%ACnh-%E1%BA%A3nh-minh-h%E1%BB%8Da.jpg",
    "https://didongviet.vn/dchannel/wp-content/uploads/2023/08/dong-vat-hinh-nen-iphone-doc-dep-didongviet-23@2x-min-1-576x1024.jpg",
    "https://noithatbinhminh.com.vn/wp-content/uploads/2022/08/hinh-nen-4k-20.jpg",
    "https://noithatbinhminh.com.vn/wp-content/uploads/2022/08/hinh-nen-4k-21.jpg",
    "https://hoanghamobile.com/tin-tuc/wp-content/uploads/2023/07/hinh-dep-5.jpg",
    "https://noithatbinhminh.com.vn/wp-content/uploads/2022/08/anh-dep-40.jpg",
    "https://mega.com.vn/media/news/1706_hinh-nen-dien-thoai-4k96.jpg",
    "https://m.yodycdn.com/blog/anh-dep-3d-yodyvn.jpg",
    "https://images.pexels.com/photos/3369569/pexels-photo-3369569.jpeg?auto=compress&cs=tinysrgb&w=400",
    "https://images.pexels.com/photos/2072583/pexels-photo-2072583.jpeg?auto=compress&cs=tinysrgb&w=400",
    "https://images.pexels.com/photos/346529/pexels-photo-346529.jpeg?auto=compress&cs=tinysrgb&w=400",
        "https://www.pexels.com/assets/static/images/flags/vn.svg",
        "https://www.pexels.com/assets/static/images/flags/us.svg",
        "https://images.pexels.com/lib/canva-growth-after-download/canva.png?w=95&dpr=1",
        "https://images.pexels.com/lib/canva-growth-after-download/collage/add%20filters%20effects.png?w=110&dpr=1",
        "https://images.pexels.com/lib/canva-growth-after-download/collage/make%20beautiful%20presentations.png?w=110&dpr=1",
        "https://images.pexels.com/lib/canva-growth-after-download/collage/create%20photo%20collages.png?w=110&dpr=1",
        "https://images.pexels.com/lib/canva-growth-after-download/collage/add%20custom%20frames.png?w=110&dpr=1",
        "https://images.pexels.com/lib/canva-growth-after-download/further/further.png?w=600&dpr=1",
        "https://images.pexels.com/lib/canva-growth-after-download/trim-and-split/trim-and-split.png?h=235&dpr=1",
        "https://images.pexels.com/photos/346529/pexels-photo-346529.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2072583/pexels-photo-2072583.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4099235/pexels-photo-4099235.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3218467/pexels-photo-3218467.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2954199/pexels-photo-2954199.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3089843/pexels-photo-3089843.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4397899/pexels-photo-4397899.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3201763/pexels-photo-3201763.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2682318/pexels-photo-2682318.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4045598/pexels-photo-4045598.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3178875/pexels-photo-3178875.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3621486/pexels-photo-3621486.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1478259/pexels-photo-1478259.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3560137/pexels-photo-3560137.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1841709/pexels-photo-1841709.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4397924/pexels-photo-4397924.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2085403/pexels-photo-2085403.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1852078/pexels-photo-1852078.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4154613/pexels-photo-4154613.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6211137/pexels-photo-6211137.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6258910/pexels-photo-6258910.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4134783/pexels-photo-4134783.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/16618972/pexels-photo-16618972/free-photo-of-phong-c-nh-thien-nhien-t-i-ky-ten.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6852786/pexels-photo-6852786.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4403896/pexels-photo-4403896.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1024864/pexels-photo-1024864.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6177597/pexels-photo-6177597.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/9843189/pexels-photo-9843189.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/30194556/pexels-photo-30194556/free-photo-of-m-ng-nh-n-den-tr-ng-thanh-l-ch-tren-hoa-d-i.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3614824/pexels-photo-3614824.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/16576798/pexels-photo-16576798/free-photo-of-mon-an-dia-anh-sang-ngh-thu-t.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/19036552/pexels-photo-19036552/free-photo-of-m-ng-nh-n-v-i-nh-ng-gi-t-m-a-tren-d-ng-c.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/28462915/pexels-photo-28462915/free-photo-of-m-ng-nh-n-d-m-s-ng-trong-bong-t-i-u-am.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/19281837/pexels-photo-19281837/free-photo-of-con-nh-n-nh-dang-d-t-l-i.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/11098770/pexels-photo-11098770.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3614821/pexels-photo-3614821.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/29824300/pexels-photo-29824300/free-photo-of-m-t-ti-n-hi-n-d-i-saudi-arabia-v-i-thi-t-k-tr-u-t-ng.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/9720495/pexels-photo-9720495.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/27681456/pexels-photo-27681456/free-photo-of-thien-nhien-r-ng-m-u-k-t-c-u.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/5977567/pexels-photo-5977567.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/30203510/pexels-photo-30203510/free-photo-of-con-nh-n-l-ng.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3095465/pexels-photo-3095465.png?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/17483867/pexels-photo-17483867/free-photo-of-tr-u-t-ng-cong-ngh-nghien-c-u-k-thu-t-s.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7594088/pexels-photo-7594088.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4195329/pexels-photo-4195329.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3018978/pexels-photo-3018978.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/28512228/pexels-photo-28512228.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1779487/pexels-photo-1779487.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3854478/pexels-photo-3854478.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6800788/pexels-photo-6800788.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3770354/pexels-photo-3770354.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/5480687/pexels-photo-5480687.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/968299/pexels-photo-968299.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/8490203/pexels-photo-8490203.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3585001/pexels-photo-3585001.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2180092/pexels-photo-2180092.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3498984/pexels-photo-3498984.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7291746/pexels-photo-7291746.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4544720/pexels-photo-4544720.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2066896/pexels-photo-2066896.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2794868/pexels-photo-2794868.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/276438/pexels-photo-276438.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6373499/pexels-photo-6373499.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2944895/pexels-photo-2944895.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/14038165/pexels-photo-14038165.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3736829/pexels-photo-3736829.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://media.istockphoto.com/id/1140691099/vi/anh/ng%C6%B0%E1%BB%9Di-ph%E1%BB%A5-n%E1%BB%AF-tr%E1%BA%BB-s%E1%BB%AD-d%E1%BB%A5ng-m%C3%A1y-t%C3%ADnh-x%C3%A1ch-tay-thi%E1%BA%BFt-k%E1%BA%BF-%C4%91%E1%BB%93-h%E1%BB%8Da-thi%E1%BA%BFt-k%E1%BA%BF-ux.jpg?b=1&s=612x612&w=0&k=20&c=GfgfRtm-1Q2WfHUCanJ3zFjfLo0-8GPV5AhyCGeSZHk=",
        "https://media.istockphoto.com/id/1091554614/vi/anh/ng%C6%B0%E1%BB%9Di-ph%E1%BB%A5-n%E1%BB%AF-xem-trang-web-c%E1%BB%A7a-m%C3%ACnh-tr%C3%AAn-m%C3%A1y-t%C3%ADnh-b%E1%BA%A3ng-v%C3%A0-m%C3%A1y-t%C3%ADnh-x%C3%A1ch-tay.jpg?b=1&s=612x612&w=0&k=20&c=mtrjY-qxKuxOb4jixbBzrcEoluwePtlBfFWZ_d_GAXE=",
        "https://media.istockphoto.com/id/1194783078/vi/anh/kh%C3%A1i-ni%E1%BB%87m-d%E1%BB%8Bch-v%E1%BB%A5-m%E1%BA%A1ng-x%C3%A3-h%E1%BB%99i-ph%C3%A1t-tr%E1%BB%B1c-tuy%E1%BA%BFn-video-th%C6%B0-vi%E1%BB%87n-video.jpg?b=1&s=612x612&w=0&k=20&c=Qyh7keBFu9Ap0dsdGilTwe2akuyxyv-YkXEPt7noWV4=",
        "https://media.istockphoto.com/id/1061329208/vi/anh/thi%E1%BA%BFt-b%E1%BB%8B-%C4%91%C3%A1p-%E1%BB%A9ng-tr%C3%AAn-trang-web-s%C3%A1ng-t%E1%BA%A1o-kh%C3%B4ng-gian-l%C3%A0m-vi%E1%BB%87c-thi%E1%BA%BFt-k%E1%BA%BF-%C4%91%E1%BB%93-h%E1%BB%8Da.jpg?b=1&s=612x612&w=0&k=20&c=sRr3_myUcgm34mVNOq0cxSO-PO-1iii-6DtqJT_qs4g=",
        "https://images.pexels.com/photos/6941458/pexels-photo-6941458.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7760991/pexels-photo-7760991.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4670294/pexels-photo-4670294.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6373497/pexels-photo-6373497.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1620765/pexels-photo-1620765.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1477833/pexels-photo-1477833.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6143808/pexels-photo-6143808.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7067138/pexels-photo-7067138.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/5422784/pexels-photo-5422784.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3312242/pexels-photo-3312242.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6177634/pexels-photo-6177634.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/8731350/pexels-photo-8731350.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7310247/pexels-photo-7310247.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/8533389/pexels-photo-8533389.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/14672475/pexels-photo-14672475.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/5948371/pexels-photo-5948371.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3965514/pexels-photo-3965514.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/28834354/pexels-photo-28834354/free-photo-of-cocktail.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/26183264/pexels-photo-26183264/free-photo-of-diana.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/432831/pexels-photo-432831.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/60036/schilfradspinne-spider-larinioides-cornutus-arachnid-60036.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1406747/pexels-photo-1406747.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1328313/pexels-photo-1328313.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/133634/pexels-photo-133634.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6610217/pexels-photo-6610217.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/5185170/pexels-photo-5185170.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6491967/pexels-photo-6491967.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/8484307/pexels-photo-8484307.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/8385406/pexels-photo-8385406.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7692136/pexels-photo-7692136.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/8087730/pexels-photo-8087730.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3369569/pexels-photo-3369569.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3368816/pexels-photo-3368816.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2825749/pexels-photo-2825749.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2179205/pexels-photo-2179205.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3951307/pexels-photo-3951307.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/832982/pexels-photo-832982.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3178938/pexels-photo-3178938.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2267563/pexels-photo-2267563.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4134791/pexels-photo-4134791.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3178852/pexels-photo-3178852.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1533721/pexels-photo-1533721.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1426715/pexels-photo-1426715.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1870094/pexels-photo-1870094.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3246155/pexels-photo-3246155.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1967539/pexels-photo-1967539.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4183079/pexels-photo-4183079.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4183505/pexels-photo-4183505.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6012907/pexels-photo-6012907.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/16509002/pexels-photo-16509002/free-photo-of-bi-n-phong-c-nh-thien-nhien-b-u-tr-i.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3958576/pexels-photo-3958576.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/18189089/pexels-photo-18189089/free-photo-of-dan-ba-sang-t-o-tr-u-t-ng-m-h.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4108271/pexels-photo-4108271.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/861412/pexels-photo-861412.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7129617/pexels-photo-7129617.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/21374997/pexels-photo-21374997/free-photo-of-den-va-tr-ng-con-trung-nh-n-web.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/24347621/pexels-photo-24347621/free-photo-of-den-va-tr-ng-ngh-thu-t-may-nh-internet.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/19115315/pexels-photo-19115315/free-photo-of-anh-sang-thien-nhien-t-i-r-ng.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/5615589/pexels-photo-5615589.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/14530413/pexels-photo-14530413.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/23831279/pexels-photo-23831279/free-photo-of-den-va-tr-ng-t-i-m-ng-nh-n-web.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/16576806/pexels-photo-16576806/free-photo-of-mon-an-dia-mau-d-ngh-thu-t.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/16885035/pexels-photo-16885035/free-photo-of-ngh-thu-t-m-u-tr-n-nha-sang.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/5652023/pexels-photo-5652023.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/30218987/pexels-photo-30218987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/30194544/pexels-photo-30194544/free-photo-of-tr-u-t-ng-den-tr-ng.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/205316/pexels-photo-205316.png?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/15863000/pexels-photo-15863000/free-photo-of-internet-cong-ngh-may-vi-tinh-logo.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2091017/pexels-photo-2091017.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7864530/pexels-photo-7864530.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/30165976/pexels-photo-30165976/free-photo-of-ma-n-canh-th-i-trang-sanh-di-u-v-i-den-chi-u-sang.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/29503185/pexels-photo-29503185/free-photo-of-m-ng-nh-n-ph-c-t-p-vao-ban-dem.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/5422855/pexels-photo-5422855.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/5417957/pexels-photo-5417957.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6800789/pexels-photo-6800789.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3683586/pexels-photo-3683586.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/205421/pexels-photo-205421.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/8567869/pexels-photo-8567869.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/12882909/pexels-photo-12882909.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4993283/pexels-photo-4993283.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/34225/spider-web-with-water-beads-network-dewdrop.jpg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/11674286/pexels-photo-11674286.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/4663318/pexels-photo-4663318.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1224709/pexels-photo-1224709.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/5422613/pexels-photo-5422613.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7486822/pexels-photo-7486822.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6801191/pexels-photo-6801191.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/206901/pexels-photo-206901.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/270669/pexels-photo-270669.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/104327/pexels-photo-104327.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7111153/pexels-photo-7111153.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/8458887/pexels-photo-8458887.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/5836193/pexels-photo-5836193.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/14268977/pexels-photo-14268977.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2081322/pexels-photo-2081322.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7111170/pexels-photo-7111170.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3585032/pexels-photo-3585032.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7067139/pexels-photo-7067139.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/5435184/pexels-photo-5435184.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/8458916/pexels-photo-8458916.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/13542616/pexels-photo-13542616.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/10752141/pexels-photo-10752141.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/8458882/pexels-photo-8458882.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7350906/pexels-photo-7350906.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1616783/pexels-photo-1616783.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/583345/pexels-photo-583345.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/25916060/pexels-photo-25916060/free-photo-of-con-nh-n-l-n-treo-minh-tren-m-ng.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/776186/pexels-photo-776186.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/1494626/pexels-photo-1494626.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/3350239/pexels-photo-3350239.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2132034/pexels-photo-2132034.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/276242/pexels-photo-276242.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/279467/pexels-photo-279467.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/57452/small-spider-spider-arachnids-close-57452.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/60011/golden-orb-spider-spider-spider-web-nature-60011.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/558796/pexels-photo-558796.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/861418/pexels-photo-861418.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/2881280/pexels-photo-2881280.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7014282/pexels-photo-7014282.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6038194/pexels-photo-6038194.png?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/8217300/pexels-photo-8217300.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7718833/pexels-photo-7718833.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/11417872/pexels-photo-11417872.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://cdn-au.onetrust.com/logos/3dbea99f-abc0-4dbd-bcd7-8f6dfcaea28d/08d31c24-1bed-4774-903b-b1725205a842/bb79b0fe-48e3-427c-bbac-47fc621af04c/3IX0JssK_400x400.jpeg",
        "https://cdn-au.onetrust.com/logos/static/powered_by_logo.svg"
)

data class HomeScreenViewModelData(
    val listFeed : List<Feed> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val imageLink: String? = null,
    val avatarLink: String? = null,
    val status: String? = null,
    val numBerHeart: Int? = 0,
    val userName: String? = null,
)
data class Feed(
    val imageLink: String? = null,
    val avatarLink: String? = null,
    val status: String? = null,
    val numBerHeart: Int? = 0,
    val userName: String? = null,
)
// class HomeScreenViewModelFactory : ViewModelProvider.Factory {
//         override fun <T : ViewModel> create(modelClass: Class<T>): T {
//             return if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
//                 HomeScreenViewModel() as T
//             } else {
//                 throw IllegalArgumentException("ViewModel Not Found")
//             }
//         }
// }
class HomeScreenViewModel : ViewModel() {
    private val viewModelState = MutableStateFlow(HomeScreenViewModelData())
    val uiState: StateFlow<HomeScreenViewModelData> = viewModelState
//        .onStart {
//            loadImages()
//            Log.e("aaa","onStart")
//        }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.Eagerly,
//            viewModelState.value
//        )
    companion object {
        private const val PAGE_SIZE = 10
        private const val LOAD_DELAY_MS = 2000L
    }
    private var loadedCount = 0
    fun loadMoreIfNeeded(currentIndex: Int) {
        if (currentIndex >= viewModelState.value.listFeed.size - 1 && !viewModelState.value.isLoading) {
            loadImages()
        }
    }
    fun loadImages() {
        if (viewModelState.value.isLoading || loadedCount >= allImages.size) return

        viewModelScope.launch {
            viewModelState.update { it.copy(isLoading = true, error = null) }

            try {
                delay(LOAD_DELAY_MS)
                val newImages = allImages
                    .drop(loadedCount)
                    .take(PAGE_SIZE)
                    .map { Feed(it) }
                if (newImages.isNotEmpty()) {
                viewModelState.update { currentState ->
                    currentState.copy(
                        listFeed = (currentState.listFeed + newImages).distinctBy { it.imageLink },
                        isLoading = false
                    )
                }
                    loadedCount += newImages.size
                } else {
            viewModelState.update { it.copy(isLoading = false) }
        }

            } catch (e: Exception) {
                viewModelState.update { currentState ->
                    currentState.copy(isLoading = false, error = e.localizedMessage)
                }
                Log.e("HomeScreenViewModel", "Error loading images: ${e.localizedMessage}")
            }
        }
    }
}