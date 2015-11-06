define(['facebook'], function(){
    FB.init({
        appId      : '404695739731135',
        version    : 'v2.4'
    });
    FB.getLoginStatus(function(response) {
        console.log(response);
    });
});
