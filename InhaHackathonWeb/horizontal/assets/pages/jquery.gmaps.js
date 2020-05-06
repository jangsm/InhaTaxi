/**
* Theme: Highdmin - Responsive Bootstrap 4 Admin Dashboard
* Author: Coderthemes
* Google Maps
*/

!function ($) {

  var contentOthers = '<div class="gmaps-overlay-message"><div class="status_others_header">기타</div> <div class="status_message_bold">발생시간</div><div class="status_message_normal">2017-07-05</div><div class="status_message_bold">차번호</div><div class="status_message_normal">77나 3364</div><div class="status_message_bold">제보</div> <div class="status_message_normal">정규식</div><div class="gmaps-overlay-message_arrow above"></div></div>';
  var contentSingal = '<div class="gmaps-overlay-message"><div class="status_signal_header">등교</div> <div class="status_message_bold">발생시간</div><div class="status_message_normal">2017-07-05</div><div class="status_message_bold">차번호</div><div class="status_message_normal">77나 3364</div><div class="status_message_bold">제보</div> <div class="status_message_normal">정규식</div><div class="gmaps-overlay-message_arrow above"></div></div>';
  var contentInterrupt = '<div class="gmaps-overlay-message"><div class="status_interrupt_header">하교</div> <div class="status_message_bold">발생시간</div><div class="status_message_normal">2017-07-05</div><div class="status_message_bold">차번호</div><div class="status_message_normal">77나 3364</div><div class="status_message_bold">제보</div> <div class="status_message_normal">정규식</div><div class="gmaps-overlay-message_arrow above"></div></div>';
  var contentLoad = '<div class="gmaps-overlay-message"><div class="status_load_header">불량적재</div> <div class="status_message_bold">발생시간</div><div class="status_message_normal">2017-07-05</div><div class="status_message_bold">차번호</div><div class="status_message_normal">77나 3364</div><div class="status_message_bold">제보</div> <div class="status_message_normal">정규식</div><div class="gmaps-overlay-message_arrow above"></div></div>';
  var contentRetribution = '<div class="gmaps-overlay-message"><div class="status_retribution_header">보복운전</div> <div class="status_message_bold">발생시간</div><div class="status_message_normal">2017-07-05</div><div class="status_message_bold">차번호</div><div class="status_message_normal">77나 3364</div><div class="status_message_bold">제보</div> <div class="status_message_normal">정규식</div><div class="gmaps-overlay-message_arrow above"></div></div>';

  var GoogleMap = function () { };


  var map;

  var otherIcon = new google.maps.MarkerImage("assets/images/others_icon.png", null, null, null, new google.maps.Size(75, 35));
  var signalIcon = new google.maps.MarkerImage("assets/images/type1.png", null, null, null, new google.maps.Size(75, 33));
  var interruptIcon = new google.maps.MarkerImage("assets/images/type2.png", null, null, null, new google.maps.Size(75, 33));
  var loadIcon = new google.maps.MarkerImage("assets/images/load_icon.png", null, null, null, new google.maps.Size(75, 34));
  var retributionIcon = new google.maps.MarkerImage("assets/images/retribution_icon.png", null, null, null, new google.maps.Size(75, 34));

  //creates map with overlay
  GoogleMap.prototype.createWithOverlay = function ($container) {

    var map = new GMaps({
      div: $container,
      lat: 37.513159,
      lng: 126.8187562,
      zoom : 11
    });

    $.ajax({
      type: "GET",
      url: "http://15.206.36.229/inha/adminRoom",
      dataType: 'text',
      contentType: "application/json; charset=utf-8",
      success: function (data) {
        var obj = JSON.parse(data);
        if (obj.code == 100) {
  
          for (var i = 0; i < obj['result'].length; i++) {
            var time = obj['result'][i]['created_at'];
            var name = obj['result'][i]['name'];
            var type = obj['result'][i]['type'];
            var lat, lon;
            var nameStr;
            if(type ==1){
              lat = obj['result'][i]['start_longitude'];
              lon = obj['result'][i]['start_latitude'];
              nameStr = obj['result'][i]['start_string'];
            }
            //등교
            else {
              lat = obj['result'][i]['end_longitude'];
              lon = obj['result'][i]['end_latitude'];
              nameStr = obj['result'][i]['end_string'];
            }
            //하교
            var dept =  obj['result'][i]['dept'];
            var user_count = obj['result'][i]['user_count'];

            // alert(time+'\n'+name+'\n'+type+'\n'+lat+'\n'+lon+'\n'+nameStr+'\n'+dept+'\n'+user_count);
            if (type == 1) {
              map.addMarker({
                lat: lat,
                lng: lon, 
                icon: signalIcon,
                infoWindow: {
                  content: '<div class="gmaps-overlay-message"><div class="status_signal_header">'+nameStr+'</div> <div class="status_message_bold">이름</div><div class="status_message_normal">'+name+'</div><div class="status_message_bold">발생시간</div><div class="status_message_normal">'+time+'</div><div class="status_message_bold">인원 수</div> <div class="status_message_normal">'+user_count+'</div><div class="gmaps-overlay-message_arrow above"></div></div>'
                }
              });
            }
        
            else if (type == 2) {
              map.addMarker({
                lat: lat,
                lng: lon, 
                icon: interruptIcon,
                infoWindow: {
                  content: '<div class="gmaps-overlay-message"><div class="status_interrupt_header">'+nameStr+'</div> <div class="status_message_bold">이름</div><div class="status_message_normal">'+name+'</div><div class="status_message_bold">발생시간</div><div class="status_message_normal">'+time+'</div><div class="status_message_bold">인원 수</div> <div class="status_message_normal">'+user_count+'</div><div class="gmaps-overlay-message_arrow above"></div></div>'
                }
              });
            }
          }
        }
  
      },
  
      error: function (request, status, error) {
        alert("code = " + request.status + " message = " + request.responseText + " error = " + error); // 실패 시 처리
      }
  
    });

    






    



    return map;
  },
    //init
    GoogleMap.prototype.init = function () {
      var $this = this;
      $(document).ready(function () {
        //creating basic map
        //with sample markers
        $this.createWithOverlay('#gmaps-overlay');
      });
    },
    //init
    $.GoogleMap = new GoogleMap, $.GoogleMap.Constructor = GoogleMap
}(window.jQuery),

  //initializing 
  function ($) {
    "use strict";
    $.GoogleMap.init()
  }(window.jQuery);