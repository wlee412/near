$(function() {
	$(".dl-btn").click(function() {
	  const filename = $(this).attr("id");

	  // <a href="/vid/rec/{filename}"> 링크 생성
	  const a = document.createElement("a");
	  a.href = `/vid/rec/${filename}`;
	  a.style.display = "none";
	  document.body.appendChild(a);
	  a.click();
	  document.body.removeChild(a);
	});
});
