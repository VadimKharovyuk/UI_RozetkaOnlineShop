/**
 * Модуль для отображения всплывающих уведомлений
 *
 * Позволяет показывать информационные сообщения
 * с автоматическим исчезновением
 */

// Публичный объект для доступа к функциям модуля
window.Notifications = {};

/**
 * Показывает всплывающее уведомление
 * @param {string} message - Текст уведомления
 * @param {Object} options - Опции уведомления (необязательно)
 * @param {number} options.duration - Длительность показа в мс (по умолчанию 3000)
 * @param {string} options.position - Позиция (bottomRight, bottomLeft, topRight, topLeft)
 * @param {string} options.type - Тип уведомления (info, success, warning, error)
 */
Notifications.show = function(message, options = {}) {
    // Настройки по умолчанию
    const settings = {
        duration: options.duration || 3000,
        position: options.position || 'bottomRight',
        type: options.type || 'info'
    };

    // Создаем элемент уведомления
    const notification = document.createElement('div');
    notification.className = 'notification';
    notification.textContent = message;

    // Добавляем класс типа уведомления, если он указан
    if (settings.type) {
        notification.classList.add(`notification-${settings.type}`);
    }

    // Базовые стили
    notification.style.position = 'fixed';
    notification.style.padding = '12px 20px';
    notification.style.borderRadius = '4px';
    notification.style.zIndex = '9999';
    notification.style.backgroundColor = 'rgba(0, 0, 0, 0.8)';
    notification.style.color = 'white';
    notification.style.boxShadow = '0 2px 10px rgba(0, 0, 0, 0.2)';
    notification.style.transform = 'translateY(20px)';
    notification.style.opacity = '0';
    notification.style.transition = 'all 0.3s ease';

    // Применяем стили в зависимости от типа
    switch (settings.type) {
        case 'success':
            notification.style.backgroundColor = 'rgba(40, 167, 69, 0.9)';
            break;
        case 'warning':
            notification.style.backgroundColor = 'rgba(255, 193, 7, 0.9)';
            notification.style.color = '#212529';
            break;
        case 'error':
            notification.style.backgroundColor = 'rgba(220, 53, 69, 0.9)';
            break;
    }

    // Устанавливаем позицию
    switch (settings.position) {
        case 'bottomRight':
            notification.style.bottom = '20px';
            notification.style.right = '20px';
            break;
        case 'bottomLeft':
            notification.style.bottom = '20px';
            notification.style.left = '20px';
            break;
        case 'topRight':
            notification.style.top = '20px';
            notification.style.right = '20px';
            break;
        case 'topLeft':
            notification.style.top = '20px';
            notification.style.left = '20px';
            break;
    }

    // Добавляем в DOM
    document.body.appendChild(notification);

    // Показываем с анимацией
    setTimeout(() => {
        notification.style.transform = 'translateY(0)';
        notification.style.opacity = '1';
    }, 10);

    // Скрываем и удаляем через указанное время
    setTimeout(() => {
        notification.style.opacity = '0';
        notification.style.transform = 'translateY(20px)';

        setTimeout(() => {
            if (notification.parentNode) {
                document.body.removeChild(notification);
            }
        }, 300); // Время на завершение анимации
    }, settings.duration);

    // Возвращаем ссылку на элемент для возможности управления извне
    return notification;
};

/**
 * Показывает уведомление успеха
 * @param {string} message - Текст уведомления
 * @param {Object} options - Дополнительные опции
 */
Notifications.success = function(message, options = {}) {
    options.type = 'success';
    return Notifications.show(message, options);
};

/**
 * Показывает уведомление с предупреждением
 * @param {string} message - Текст уведомления
 * @param {Object} options - Дополнительные опции
 */
Notifications.warning = function(message, options = {}) {
    options.type = 'warning';
    return Notifications.show(message, options);
};

/**
 * Показывает уведомление об ошибке
 * @param {string} message - Текст уведомления
 * @param {Object} options - Дополнительные опции
 */
Notifications.error = function(message, options = {}) {
    options.type = 'error';
    return Notifications.show(message, options);
};