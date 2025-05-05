/**
 * Модуль для обработки действий с карточками категорий
 * Обновленная версия с поддержкой прямого перехода по ссылкам
 */
window.CardActions = {};

/**
 * Инициализация действий с карточками
 */
CardActions.init = function() {
    // Обработка кнопок-иконок
    document.querySelectorAll('.action-icon').forEach(button => {
        button.addEventListener('click', (e) => {
            const icon = button.querySelector('i');

            // Если это ссылка на категорию с атрибутом data-redirect, позволяем стандартное поведение
            if (button.hasAttribute('data-redirect') && button.getAttribute('data-redirect') === 'true') {
                // Не делаем ничего, позволяя стандартному поведению ссылки сработать
                return true;
            }

            // Для всех остальных иконок предотвращаем стандартное поведение
            e.preventDefault();
            e.stopPropagation();

            if (icon.classList.contains('fa-heart')) {
                // Обработка добавления в избранное
                CardActions.toggleFavorite(icon);
            } else if (icon.classList.contains('fa-eye') && !button.hasAttribute('data-redirect')) {
                // Для иконок глаза без атрибута data-redirect используем модальное окно (если нужно)
                const card = button.closest('.category-card') || button.closest('.product-card');
                if (card) {
                    const title = card.querySelector('.category-title') || card.querySelector('.product-title');
                    if (title) {
                        if (window.ModalWindows && typeof window.ModalWindows.showQuickView === 'function') {
                            window.ModalWindows.showQuickView(title.textContent);
                        }
                    }
                }
            } else if (icon.classList.contains('fa-shopping-cart')) {
                // Обработка добавления в корзину
                if (window.Notifications && typeof window.Notifications.show === 'function') {
                    window.Notifications.show('Товар добавлен в корзину');
                }
            }
        });
    });

    // Дополнительная обработка для иконок с data-redirect
    document.querySelectorAll('.action-icon[data-redirect="true"]').forEach(link => {
        // Добавляем визуальный эффект при наведении для лучшего UX
        link.addEventListener('mouseenter', function() {
            this.style.opacity = '0.8';
        });

        link.addEventListener('mouseleave', function() {
            this.style.opacity = '1';
        });
    });
};

/**
 * Переключение состояния избранного
 * @param {HTMLElement} icon - Иконка сердечка
 */
CardActions.toggleFavorite = function(icon) {
    if (icon.classList.contains('far')) {
        icon.classList.remove('far');
        icon.classList.add('fas');

        if (window.Notifications && typeof window.Notifications.show === 'function') {
            window.Notifications.show('Добавлено в избранное');
        }
    } else {
        icon.classList.remove('fas');
        icon.classList.add('far');

        if (window.Notifications && typeof window.Notifications.show === 'function') {
            window.Notifications.show('Удалено из избранного');
        }
    }
};

// Автоматическая инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    CardActions.init();
});