/**
 * Модуль для обработки действий с карточками товаров и категорий
 *
 * Функциональность:
 * - Добавление/удаление из избранного
 * - Быстрый просмотр категорий/товаров
 */

// Публичный объект для доступа к функциям модуля
window.CardActions = {};

/**
 * Инициализация действий с карточками
 */
CardActions.init = function() {
    document.querySelectorAll('.action-icon').forEach(button => {
        button.addEventListener('click', (e) => {
            e.preventDefault();
            e.stopPropagation();

            const icon = button.querySelector('i');

            if (icon.classList.contains('fa-heart')) {
                CardActions.toggleFavorite(icon);
            } else if (icon.classList.contains('fa-eye')) {
                const card = button.closest('.category-card') || button.closest('.product-card');
                if (card) {
                    const title = card.querySelector('.category-title') || card.querySelector('.product-title');
                    if (title) {
                        // Используем функцию быстрого просмотра из модуля модальных окон
                        if (window.ModalWindows && typeof window.ModalWindows.showQuickView === 'function') {
                            window.ModalWindows.showQuickView(title.textContent);
                        }
                    }
                }
            } else if (icon.classList.contains('fa-shopping-cart')) {
                // Добавление в корзину
                if (window.Notifications && typeof window.Notifications.show === 'function') {
                    window.Notifications.show('Товар добавлен в корзину');
                }
            }
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