
 function openTab(index) {
      const tabs = document.querySelectorAll('.tab-content');
      const buttons = document.querySelectorAll('.tab-button');
      tabs.forEach(t => t.classList.remove('active'));
      buttons.forEach(b => b.classList.remove('active'));
      tabs[index].classList.add('active');
      buttons[index].classList.add('active');
}

let currentIndex = 0;

function moveSlide(direction) {
  const track = document.querySelector('.slider-track');
  const totalSlides = document.querySelectorAll('.slider-card').length;

  currentIndex += direction;

  if (currentIndex < 0) currentIndex = 0;
  if (currentIndex >= totalSlides) currentIndex = totalSlides - 1;

  const offset = -currentIndex * 100;
  track.style.transform = `translateX(${offset}%)`;
}
	
	