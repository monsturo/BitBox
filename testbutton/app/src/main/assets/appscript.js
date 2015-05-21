$(document).ready(function(){
  $('#container').load('main_menu.html', function(){
    $('#container').fadeTo( 1, 1, function(){
      $('#image').css('opacity', '0.5');
    });
  });
  console.log("Main menu loaded...");
});

/* HIGHSCORE LISTE */
function loadScores(){
  $('#container').fadeOut( 400, 
    function getHighscores(){
    if (window.XMLHttpRequest){
      // code for IE7+, Firefox, Chrome, Opera, Safari
      xmlhttp=new XMLHttpRequest();
    } else {
      // code for IE6, IE5
      xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function()
    {
      if (xmlhttp.readyState==4 && xmlhttp.status==200){
        document.getElementById("container").innerHTML=xmlhttp.responseText;
      }
    }
    xmlhttp.open("GET","highscores.php",true);
    xmlhttp.send();
  });
  $('#container').fadeIn( 400 );
};

/* HOME KNAP */
function loadHome() {
  $('#container').fadeOut( 400, function(){ 
    $('#container').load('main_menu.html');
  });
  $('#container').fadeIn( 400, function(){
      $('#image').fadeTo(400, 0.5)
      $('#image').css('opacity', '0.5');});
};

/* EXPANSIONS */
function loadPacks() {
  $('#container').fadeOut( 400, function(){
    $('#container').load('expansions.html');
  });
  $('#container').fadeIn( 400 );
};

/* ACHIEVEMENTS */
function loadAchievs() {
  $('#container').fadeOut( 400, function(){
    $('#container').load('achievements.html');
  });
  $('#container').fadeIn( 400 );
};
