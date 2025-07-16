
 function openTab(index) {
      const tabs = document.querySelectorAll('.tab-content');
      const buttons = document.querySelectorAll('.tab-button');
      tabs.forEach(t => t.classList.remove('active'));
      buttons.forEach(b => b.classList.remove('active'));
      tabs[index].classList.add('active');
      buttons[index].classList.add('active');
}


let currentIndex = 0;
const track = document.getElementById('sliderTrack');
const cards = Array.from(track.children);
const btnPrev = document.querySelector('.slider-button.prev');
const btnNext = document.querySelector('.slider-button.next');

function updateTrack() {
  const card = document.querySelector('.slider-card');
  const wrapper = document.querySelector('.slider-wrapper');
  const track = document.getElementById('sliderTrack');
  if (!card || !track || !wrapper) return;
  const cardWidth = card.offsetWidth;
  const wrapperWidth = wrapper.offsetWidth;
  // 중앙 정렬을 위해 카드 왼쪽이 wrapper 중앙과 일치하게 이동
  const offset = (wrapperWidth - cardWidth) / 2;
  track.style.transform = `translateX(${-currentIndex * cardWidth + offset}px)`;
}


function updateActiveClasses() {
  cards.forEach((card, idx) => {
    if (idx === currentIndex) {
      card.classList.add('active');
      card.classList.remove('inactive');
    } else {
      card.classList.add('inactive');
      card.classList.remove('active');
    }
  });
}

function moveSlide(direction) {
  currentIndex = Math.min(Math.max(0, currentIndex + direction), cards.length - 1);
  updateTrack();
  updateActiveClasses();
}

btnPrev.addEventListener('click', () => moveSlide(-1));
btnNext.addEventListener('click', () => moveSlide(1));

// 초기화
document.addEventListener('DOMContentLoaded', () => {
  updateTrack();
  updateActiveClasses();
});

	
	