// carousel.js - Функции для работы с каруселью товаров

document.addEventListener('DOMContentLoaded', function() {
    // Элементы карусели
    const carousel = document.querySelector('.products-carousel');
    const slides = document.querySelectorAll('.product-slide');
    const prevButton = document.querySelector('.carousel-prev');
    const nextButton = document.querySelector('.carousel-next');
    const dots = document.querySelectorAll('.carousel-dots .dot');

    // Если на странице нет карусели, выходим
    if (!carousel || !slides.length) return;

    let currentIndex = 0;
    let slideWidth = 0;
    let visibleSlides = 4;

    // Функция для обновления карусели
    function updateCarousel() {
        // Определение количества видимых слайдов в зависимости от ширины экрана
        if (window.innerWidth < 576) {
            visibleSlides = 1;
        } else if (window.innerWidth < 768) {
            visibleSlides = 2;
        } else if (window.innerWidth < 992) {
            visibleSlides = 3;
        } else {
            visibleSlides = 4;
        }

        // Обновление размеров и позиции
        slideWidth = carousel.clientWidth / visibleSlides;
        slides.forEach(slide => {
            slide.style.minWidth = `${slideWidth}px`;
        });

        moveToSlide(currentIndex);
    }

    // Функция для перемещения к определенному слайду
    function moveToSlide(index) {
        if (index < 0) {
            index = 0;
        } else if (index > slides.length - visibleSlides) {
            index = slides.length - visibleSlides;
        }

        currentIndex = index;
        carousel.style.transform = `translateX(-${currentIndex * slideWidth}px)`;

        // Обновление активной точки
        const activeDotIndex = Math.floor(currentIndex / visibleSlides);
        dots.forEach((dot, i) => {
            dot.classList.toggle('active', i === activeDotIndex);
        });

        // Скрытие/отображение кнопок при достижении границ
        prevButton.style.opacity = currentIndex === 0 ? '0.5' : '1';
        nextButton.style.opacity = currentIndex >= slides.length - visibleSlides ? '0.5' : '1';
    }

    // Обработчики событий для кнопок
    prevButton.addEventListener('click', () => {
        moveToSlide(currentIndex - 1);
    });

    nextButton.addEventListener('click', () => {
        moveToSlide(currentIndex + 1);
    });

    // Обработчики событий для точек
    dots.forEach((dot, i) => {
        dot.addEventListener('click', () => {
            moveToSlide(i * visibleSlides);
        });
    });

    // Обработчики для кнопок действий товаров
    document.querySelectorAll('.product-overlay .action-icon').forEach(button => {
        button.addEventListener('click', (e) => {
            e.preventDefault();
            e.stopPropagation();

            const icon = button.querySelector('i');

            if (icon.classList.contains('fa-heart')) {
                // Избранное
                if (icon.classList.contains('far')) {
                    icon.classList.remove('far');
                    icon.classList.add('fas');
                    showNotification('Добавлено в избранное');
                } else {
                    icon.classList.remove('fas');
                    icon.classList.add('far');
                    showNotification('Удалено из избранного');
                }
            } else if (icon.classList.contains('fa-shopping-cart')) {
                // Корзина
                showNotification('Товар добавлен в корзину');
            } else if (icon.classList.contains('fa-eye')) {
                // Быстрый просмотр
                const productTitle = button.closest('.product-card').querySelector('.product-title').textContent;
                showNotification(`Быстрый просмотр: ${productTitle}`);
            }
        });
    });

    // Инициализация карусели и обновление при изменении размера окна
    updateCarousel();
    window.addEventListener('resize', updateCarousel);
});