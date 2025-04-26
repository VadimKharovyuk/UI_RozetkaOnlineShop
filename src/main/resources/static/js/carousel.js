/**
 * Скрипт для управления каруселью товаров
 *
 * Функции:
 * - Адаптивное отображение слайдов в зависимости от ширины экрана
 * - Управление карусельной навигацией (кнопки, точки)
 * - Обработка действий с товарами (добавление в избранное, корзину и т.д.)
 */

// Инициализация карусели при загрузке страницы
function initProductCarousel() {
    // Элементы карусели
    const carousel = document.querySelector('.products-carousel');
    const slides = document.querySelectorAll('.product-slide');
    const prevButton = document.querySelector('.carousel-prev');
    const nextButton = document.querySelector('.carousel-next');
    const dots = document.querySelectorAll('.carousel-dots .dot');

    // Если элементы карусели не найдены, выход из функции
    if (!carousel || !slides.length) {
        return;
    }

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
        if (dots && dots.length) {
            const activeDotIndex = Math.floor(currentIndex / visibleSlides);
            dots.forEach((dot, i) => {
                dot.classList.toggle('active', i === activeDotIndex);
            });
        }

        // Скрытие/отображение кнопок при достижении границ
        if (prevButton) {
            prevButton.style.opacity = currentIndex === 0 ? '0.5' : '1';
        }
        if (nextButton) {
            nextButton.style.opacity = currentIndex >= slides.length - visibleSlides ? '0.5' : '1';
        }
    }

    // Обработчики событий для кнопок
    if (prevButton) {
        prevButton.addEventListener('click', () => {
            moveToSlide(currentIndex - 1);
        });
    }

    if (nextButton) {
        nextButton.addEventListener('click', () => {
            moveToSlide(currentIndex + 1);
        });
    }

    // Обработчики событий для точек
    if (dots && dots.length) {
        dots.forEach((dot, i) => {
            dot.addEventListener('click', () => {
                moveToSlide(i * visibleSlides);
            });
        });
    }

    // Инициализация карусели и обновление при изменении размера окна
    updateCarousel();
    window.addEventListener('resize', updateCarousel);
}

// Функция для отображения уведомлений
function showNotification(message) {
    const notification = document.createElement('div');
    notification.className = 'notification';
    notification.textContent = message;

    notification.style.position = 'fixed';
    notification.style.bottom = '20px';
    notification.style.right = '20px';
    notification.style.backgroundColor = 'rgba(0, 0, 0, 0.8)';
    notification.style.color = 'white';
    notification.style.padding = '12px 20px';
    notification.style.borderRadius = '4px';
    notification.style.zIndex = '9999';
    notification.style.transform = 'translateY(100px)';
    notification.style.opacity = '0';
    notification.style.transition = 'all 0.3s ease';

    document.body.appendChild(notification);

    setTimeout(() => {
        notification.style.transform = 'translateY(0)';
        notification.style.opacity = '1';
    }, 10);

    setTimeout(() => {
        notification.style.opacity = '0';
        notification.style.transform = 'translateY(100px)';

        setTimeout(() => {
            document.body.removeChild(notification);
        }, 300);
    }, 3000);
}

// Инициализация обработчиков для кнопок действий товаров
function initProductActions() {
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
}

// Функция для инициализации всех компонентов
function initProductComponents() {
    initProductCarousel();
    initProductActions();
}

// Экспорт функций для внешнего использования
window.ProductCarousel = {
    init: initProductComponents,
    showNotification: showNotification
};

// Автоматическая инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', initProductComponents);