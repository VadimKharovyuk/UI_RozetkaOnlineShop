// main.js - Основные функции и утилиты для всего сайта

// DOM загружен
document.addEventListener('DOMContentLoaded', function() {
    // Общие функции для мобильного меню
    handleMobileMenu();

    // Проверка размера экрана при загрузке
    handleResponsiveLayout();

    // Обработчик изменения размера окна
    window.addEventListener('resize', handleResponsiveLayout);
});

// Функция для работы с мобильным меню
function handleMobileMenu() {
    const searchToggle = document.getElementById('searchToggle');
    const searchContainer = document.getElementById('searchContainer');

    // Обработчик мобильного поиска (если существует)
    if (searchToggle) {
        searchToggle.addEventListener('click', () => {
            searchContainer.classList.toggle('active');
        });
    }
}

// Функция для адаптивности элементов
function handleResponsiveLayout() {
    const searchContainer = document.getElementById('searchContainer');

    // Сброс состояний при изменении размера экрана
    if (window.innerWidth > 768) {
        searchContainer.classList.remove('active');
    }
}

// Общая функция для показа уведомлений
function showNotification(message) {
    // Создаем элемент уведомления
    const notification = document.createElement('div');
    notification.className = 'notification';
    notification.textContent = message;

    // Стилизуем уведомление
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

    // Добавляем уведомление на страницу
    document.body.appendChild(notification);

    // Показываем уведомление с анимацией
    setTimeout(() => {
        notification.style.transform = 'translateY(0)';
        notification.style.opacity = '1';
    }, 10);

    // Скрываем уведомление через 3 секунды
    setTimeout(() => {
        notification.style.opacity = '0';
        notification.style.transform = 'translateY(100px)';

        setTimeout(() => {
            document.body.removeChild(notification);
        }, 300);
    }, 3000);
}

// Делаем функцию доступной глобально
window.showNotification = showNotification;