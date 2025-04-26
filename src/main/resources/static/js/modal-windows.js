/**
 * Модуль для работы с модальными окнами
 *
 * Функциональность:
 * - Создание и показ модальных окон
 * - Окно быстрого просмотра категорий/товаров
 * - Поддержка анимации и разных типов модальных окон
 */

// Публичный объект для доступа к функциям модуля
window.ModalWindows = {};

/**
 * Показывает модальное окно
 * @param {Object} options - Опции модального окна
 * @param {string} options.title - Заголовок окна
 * @param {string|HTMLElement} options.content - Содержимое окна (строка HTML или DOM-элемент)
 * @param {boolean} options.showClose - Показывать кнопку закрытия (по умолчанию true)
 * @param {string} options.size - Размер окна (small, medium, large, fullscreen)
 * @param {Function} options.onClose - Функция, вызываемая при закрытии окна
 */
ModalWindows.show = function(options = {}) {
    // Настройки по умолчанию
    const settings = {
        title: options.title || '',
        content: options.content || '',
        showClose: options.showClose !== undefined ? options.showClose : true,
        size: options.size || 'medium',
        onClose: options.onClose || null
    };

    // Создаем элементы модального окна
    const modal = document.createElement('div');
    modal.className = 'modal-window';

    const modalContent = document.createElement('div');
    modalContent.className = 'modal-content';
    modalContent.classList.add(`modal-size-${settings.size}`);

    // Добавляем заголовок
    if (settings.title) {
        const title = document.createElement('h2');
        title.className = 'modal-title';
        title.textContent = settings.title;
        modalContent.appendChild(title);
    }

    // Добавляем кнопку закрытия
    if (settings.showClose) {
        const closeBtn = document.createElement('button');
        closeBtn.className = 'modal-close';
        closeBtn.innerHTML = '&times;';
        modalContent.appendChild(closeBtn);

        // Обработчик закрытия
        closeBtn.addEventListener('click', () => {
            ModalWindows.close(modal);
        });
    }

    // Добавляем содержимое
    const contentContainer = document.createElement('div');
    contentContainer.className = 'modal-content-container';

    if (typeof settings.content === 'string') {
        contentContainer.innerHTML = settings.content;
    } else if (settings.content instanceof HTMLElement) {
        contentContainer.appendChild(settings.content);
    }

    modalContent.appendChild(contentContainer);
    modal.appendChild(modalContent);

    // Стили для модального окна
    modal.style.position = 'fixed';
    modal.style.top = '0';
    modal.style.left = '0';
    modal.style.width = '100%';
    modal.style.height = '100%';
    modal.style.backgroundColor = 'rgba(0,0,0,0.7)';
    modal.style.display = 'flex';
    modal.style.alignItems = 'center';
    modal.style.justifyContent = 'center';
    modal.style.zIndex = '2001';
    modal.style.opacity = '0';
    modal.style.transition = 'opacity 0.3s ease';

    // Стили для контента модального окна
    modalContent.style.backgroundColor = '#fff';
    modalContent.style.padding = '2rem';
    modalContent.style.borderRadius = '8px';
    modalContent.style.position = 'relative';
    modalContent.style.transform = 'translateY(50px)';
    modalContent.style.transition = 'transform 0.3s ease';
    modalContent.style.maxHeight = '90vh';
    modalContent.style.overflow = 'auto';

    // Разные размеры модального окна
    switch (settings.size) {
        case 'small':
            modalContent.style.width = '90%';
            modalContent.style.maxWidth = '500px';
            break;
        case 'medium':
            modalContent.style.width = '90%';
            modalContent.style.maxWidth = '800px';
            break;
        case 'large':
            modalContent.style.width = '90%';
            modalContent.style.maxWidth = '1200px';
            break;
        case 'fullscreen':
            modalContent.style.width = '95%';
            modalContent.style.height = '95%';
            modalContent.style.maxWidth = 'none';
            break;
    }

    // Стили для кнопки закрытия
    if (settings.showClose) {
        const closeBtn = modalContent.querySelector('.modal-close');
        closeBtn.style.position = 'absolute';
        closeBtn.style.top = '15px';
        closeBtn.style.right = '15px';
        closeBtn.style.fontSize = '1.5rem';
        closeBtn.style.background = 'none';
        closeBtn.style.border = 'none';
        closeBtn.style.cursor = 'pointer';
        closeBtn.style.zIndex = '1';
    }

    // Стили для заголовка
    if (settings.title) {
        const title = modalContent.querySelector('.modal-title');
        title.style.marginBottom = '1rem';
        title.style.fontSize = '1.8rem';
    }

    // Добавляем в DOM
    document.body.appendChild(modal);
    document.body.style.overflow = 'hidden'; // Блокируем прокрутку

    // Закрытие по клику на фоне
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            ModalWindows.close(modal);
        }
    });

    // Закрытие по Esc
    const escHandler = (e) => {
        if (e.key === 'Escape') {
            ModalWindows.close(modal);
            document.removeEventListener('keydown', escHandler);
        }
    };
    document.addEventListener('keydown', escHandler);

    // Сохраняем callback для закрытия
    modal.onCloseCallback = settings.onClose;

    // Показываем с анимацией
    setTimeout(() => {
        modal.style.opacity = '1';
        modalContent.style.transform = 'translateY(0)';
    }, 10);

    return modal;
};

/**
 * Закрывает модальное окно
 * @param {HTMLElement} modal - Элемент модального окна
 */
ModalWindows.close = function(modal) {
    if (!modal) return;

    const modalContent = modal.querySelector('.modal-content');
    modal.style.opacity = '0';
    if (modalContent) {
        modalContent.style.transform = 'translateY(50px)';
    }

    setTimeout(() => {
        if (modal.parentNode) {
            document.body.removeChild(modal);
            document.body.style.overflow = ''; // Разблокируем прокрутку

            // Вызываем callback при закрытии
            if (typeof modal.onCloseCallback === 'function') {
                modal.onCloseCallback();
            }
        }
    }, 300); // Время на завершение анимации
};

/**
 * Показывает окно быстрого просмотра категории/товара
 * @param {string} title - Название категории/товара
 */
ModalWindows.showQuickView = function(title) {
    // Создаем содержимое окна
    const container = document.createElement('div');

    const description = document.createElement('p');
    description.textContent = `Быстрый просмотр популярных товаров из категории "${title}"`;
    description.style.marginBottom = '2rem';
    description.style.color = '#777';

    const productsGrid = document.createElement('div');
    productsGrid.className = 'quick-view-products';
    productsGrid.style.display = 'grid';
    productsGrid.style.gridTemplateColumns = 'repeat(auto-fill, minmax(200px, 1fr))';
    productsGrid.style.gap = '1.5rem';
    productsGrid.style.marginBottom = '2rem';

    // Добавляем примеры товаров
    for (let i = 0; i < 3; i++) {
        const product = document.createElement('div');
        product.className = 'quick-product';

        const imgPlaceholder = document.createElement('div');
        imgPlaceholder.className = 'product-img-placeholder';
        imgPlaceholder.style.height = '200px';
        imgPlaceholder.style.backgroundColor = '#f5f5f5';
        imgPlaceholder.style.marginBottom = '1rem';
        imgPlaceholder.style.borderRadius = '4px';

        const productTitle = document.createElement('h4');
        productTitle.textContent = `Товар ${i+1}`;

        const productPrice = document.createElement('p');
        productPrice.className = 'product-price';
        productPrice.textContent = `₽${Math.floor(Math.random() * 10000) + 1000}`;

        product.appendChild(imgPlaceholder);
        product.appendChild(productTitle);
        product.appendChild(productPrice);
        productsGrid.appendChild(product);
    }

    const viewAllBtn = document.createElement('a');
    viewAllBtn.href = '#';
    viewAllBtn.className = 'view-all-btn';
    viewAllBtn.textContent = 'Смотреть все товары';
    viewAllBtn.style.display = 'inline-block';
    viewAllBtn.style.padding = '0.8rem 1.5rem';
    viewAllBtn.style.backgroundColor = '#1a1a1a';
    viewAllBtn.style.color = '#fff';
    viewAllBtn.style.textDecoration = 'none';
    viewAllBtn.style.borderRadius = '4px';
    viewAllBtn.style.fontWeight = '500';
    viewAllBtn.style.transition = 'background-color 0.3s ease';

    container.appendChild(description);
    container.appendChild(productsGrid);
    container.appendChild(viewAllBtn);

    // Показываем модальное окно
    return ModalWindows.show({
        title: title,
        content: container,
        size: 'medium'
    });
};

/**
 * Показывает модальное окно подтверждения
 * @param {Object} options - Опции модального окна
 * @param {string} options.title - Заголовок окна
 * @param {string} options.message - Текст сообщения
 * @param {string} options.confirmText - Текст кнопки подтверждения
 * @param {string} options.cancelText - Текст кнопки отмены
 * @param {Function} options.onConfirm - Функция, вызываемая при подтверждении
 * @param {Function} options.onCancel - Функция, вызываемая при отмене
 */
ModalWindows.confirm = function(options = {}) {
    // Настройки по умолчанию
    const settings = {
        title: options.title || 'Подтверждение',
        message: options.message || 'Вы уверены?',
        confirmText: options.confirmText || 'Да',
        cancelText: options.cancelText || 'Отмена',
        onConfirm: options.onConfirm || null,
        onCancel: options.onCancel || null
    };

    // Создаем содержимое окна
    const container = document.createElement('div');

    const message = document.createElement('p');
    message.textContent = settings.message;
    message.style.marginBottom = '2rem';
    message.style.fontSize = '1.1rem';

    const buttonsContainer = document.createElement('div');
    buttonsContainer.style.display = 'flex';
    buttonsContainer.style.justifyContent = 'flex-end';
    buttonsContainer.style.gap = '1rem';

    const cancelBtn = document.createElement('button');
    cancelBtn.textContent = settings.cancelText;
    cancelBtn.style.padding = '0.5rem 1rem';
    cancelBtn.style.borderRadius = '4px';
    cancelBtn.style.border = '1px solid #ccc';
    cancelBtn.style.background = 'white';
    cancelBtn.style.cursor = 'pointer';

    const confirmBtn = document.createElement('button');
    confirmBtn.textContent = settings.confirmText;
    confirmBtn.style.padding = '0.5rem 1rem';
    confirmBtn.style.borderRadius = '4px';
    confirmBtn.style.border = 'none';
    confirmBtn.style.backgroundColor = '#1a1a1a';
    confirmBtn.style.color = 'white';
    confirmBtn.style.cursor = 'pointer';

    buttonsContainer.appendChild(cancelBtn);
    buttonsContainer.appendChild(confirmBtn);

    container.appendChild(message);
    container.appendChild(buttonsContainer);

    // Показываем модальное окно
    const modal = ModalWindows.show({
        title: settings.title,
        content: container,
        size: 'small',
        showClose: true
    });

    // Обработчики событий для кнопок
    cancelBtn.addEventListener('click', () => {
        ModalWindows.close(modal);
        if (typeof settings.onCancel === 'function') {
            settings.onCancel();
        }
    });

    confirmBtn.addEventListener('click', () => {
        ModalWindows.close(modal);
        if (typeof settings.onConfirm === 'function') {
            settings.onConfirm();
        }
    });

    return modal;
};