/**
 * Скрипт для управления интерфейсом сайта
 *
 * Функциональность:
 * - Боковое меню (открытие/закрытие/подменю)
 * - Поисковая строка на мобильных устройствах
 * - Добавление в избранное
 * - Быстрый просмотр категорий
 * - Уведомления
 */

// Инициализация всех компонентов интерфейса сайта
function initSiteInterface() {
    // Инициализация бокового меню
    initSidebar();

    // Инициализация поиска на мобильных устройствах
    initMobileSearch();

    // Инициализация действий с карточками категорий/товаров
    initCardActions();

    // Обработка изменения размера окна
    window.addEventListener('resize', handleResize);
}

// Инициализация бокового меню
function initSidebar() {
    const menuButton = document.getElementById('menuButton');
    const sidebar = document.getElementById('sidebar');
    const closeSidebar = document.getElementById('closeSidebar');
    const overlay = document.getElementById('overlay');
    const menuItems = document.querySelectorAll('.menu-item');

    // Проверяем, существуют ли элементы на странице
    if (!menuButton || !sidebar || !overlay) {
        return;
    }

    // Открытие бокового меню
    menuButton.addEventListener('click', () => {
        sidebar.classList.add('active');
        overlay.classList.add('active');
        document.body.style.overflow = 'hidden'; // Блокируем прокрутку страницы
    });

    // Функция закрытия бокового меню
    function closeSidebarMenu() {
        sidebar.classList.remove('active');
        overlay.classList.remove('active');
        document.body.style.overflow = ''; // Разрешаем прокрутку страницы
    }

    // Слушатели для закрытия меню
    if (closeSidebar) {
        closeSidebar.addEventListener('click', closeSidebarMenu);
    }
    overlay.addEventListener('click', closeSidebarMenu);

    // Обработка подменю
    menuItems.forEach(item => {
        const toggle = item.querySelector('.menu-toggle');
        if (toggle) {
            item.addEventListener('click', () => {
                const submenu = item.nextElementSibling;
                if (submenu && submenu.classList.contains('submenu')) {
                    // Закрываем все другие подменю
                    document.querySelectorAll('.submenu.active').forEach(menu => {
                        if (menu !== submenu) {
                            menu.classList.remove('active');
                            const toggleIcon = menu.previousElementSibling.querySelector('.menu-toggle');
                            if (toggleIcon) toggleIcon.classList.remove('active');
                        }
                    });

                    // Переключаем текущее подменю
                    submenu.classList.toggle('active');
                    toggle.classList.toggle('active');
                }
            });
        }
    });
}

// Инициализация поиска на мобильных устройствах
function initMobileSearch() {
    const searchToggle = document.getElementById('searchToggle');
    const searchContainer = document.getElementById('searchContainer');

    if (!searchToggle || !searchContainer) {
        return;
    }

    searchToggle.addEventListener('click', () => {
        searchContainer.classList.toggle('active');
    });
}

// Инициализация действий с карточками категорий/товаров
function initCardActions() {
    document.querySelectorAll('.action-icon').forEach(button => {
        button.addEventListener('click', (e) => {
            e.preventDefault();
            e.stopPropagation();

            const icon = button.querySelector('i');

            if (icon.classList.contains('fa-heart')) {
                // Переключение избранного
                if (icon.classList.contains('far')) {
                    icon.classList.remove('far');
                    icon.classList.add('fas');
                    showNotification('Добавлено в избранное');
                } else {
                    icon.classList.remove('fas');
                    icon.classList.add('far');
                    showNotification('Удалено из избранного');
                }
            } else if (icon.classList.contains('fa-eye')) {
                // Быстрый просмотр
                const card = button.closest('.category-card') || button.closest('.product-card');
                if (card) {
                    const title = card.querySelector('.category-title') || card.querySelector('.product-title');
                    if (title) {
                        showQuickView(title.textContent);
                    }
                }
            }
        });
    });
}

// Обработка изменения размера окна
function handleResize() {
    const searchContainer = document.getElementById('searchContainer');
    if (searchContainer && window.innerWidth > 768) {
        searchContainer.classList.remove('active');
    }
}

/**
 * Показывает всплывающее уведомление
 * @param {string} message - Текст уведомления
 */
function showNotification(message) {
    // Создаем элемент уведомления
    const notification = document.createElement('div');
    notification.className = 'notification';
    notification.textContent = message;

    // Добавляем стили
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

    // Добавляем в body
    document.body.appendChild(notification);

    // Показываем уведомление
    setTimeout(() => {
        notification.style.transform = 'translateY(0)';
        notification.style.opacity = '1';
    }, 10);

    // Удаляем через 3 секунды
    setTimeout(() => {
        notification.style.opacity = '0';
        notification.style.transform = 'translateY(100px)';

        setTimeout(() => {
            document.body.removeChild(notification);
        }, 300);
    }, 3000);
}

/**
 * Показывает модальное окно быстрого просмотра
 * @param {string} categoryName - Название категории/товара
 */
function showQuickView(categoryName) {
    // Создаем элементы модального окна
    const modal = document.createElement('div');
    modal.className = 'quick-view-modal';

    const modalContent = document.createElement('div');
    modalContent.className = 'quick-view-content';

    const closeBtn = document.createElement('button');
    closeBtn.className = 'quick-view-close';
    closeBtn.innerHTML = '&times;';

    const title = document.createElement('h2');
    title.textContent = categoryName;

    const description = document.createElement('p');
    description.textContent = `Быстрый просмотр популярных товаров из категории "${categoryName}"`;

    const productsGrid = document.createElement('div');
    productsGrid.className = 'quick-view-products';

    // Добавляем примеры товаров
    for (let i = 0; i < 3; i++) {
        const product = document.createElement('div');
        product.className = 'quick-product';

        product.innerHTML = `
            <div class="product-img-placeholder"></div>
            <h4>Товар ${i+1}</h4>
            <p class="product-price">₽${Math.floor(Math.random() * 10000) + 1000}</p>
        `;

        productsGrid.appendChild(product);
    }

    const viewAllBtn = document.createElement('a');
    viewAllBtn.href = '#';
    viewAllBtn.className = 'view-all-btn';
    viewAllBtn.textContent = 'Смотреть все товары';

    // Собираем модальное окно
    modalContent.appendChild(closeBtn);
    modalContent.appendChild(title);
    modalContent.appendChild(description);
    modalContent.appendChild(productsGrid);
    modalContent.appendChild(viewAllBtn);
    modal.appendChild(modalContent);

    // Добавляем стили
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

    modalContent.style.backgroundColor = '#fff';
    modalContent.style.padding = '2rem';
    modalContent.style.borderRadius = '8px';
    modalContent.style.width = '90%';
    modalContent.style.maxWidth = '800px';
    modalContent.style.maxHeight = '90vh';
    modalContent.style.overflow = 'auto';
    modalContent.style.position = 'relative';
    modalContent.style.transform = 'translateY(50px)';
    modalContent.style.transition = 'transform 0.3s ease';

    closeBtn.style.position = 'absolute';
    closeBtn.style.top = '15px';
    closeBtn.style.right = '15px';
    closeBtn.style.fontSize = '1.5rem';
    closeBtn.style.background = 'none';
    closeBtn.style.border = 'none';
    closeBtn.style.cursor = 'pointer';

    title.style.marginBottom = '1rem';
    title.style.fontSize = '1.8rem';

    description.style.marginBottom = '2rem';
    description.style.color = '#777';

    productsGrid.style.display = 'grid';
    productsGrid.style.gridTemplateColumns = 'repeat(auto-fill, minmax(200px, 1fr))';
    productsGrid.style.gap = '1.5rem';
    productsGrid.style.marginBottom = '2rem';

    viewAllBtn.style.display = 'inline-block';
    viewAllBtn.style.padding = '0.8rem 1.5rem';
    viewAllBtn.style.backgroundColor = '#1a1a1a';
    viewAllBtn.style.color = '#fff';
    viewAllBtn.style.textDecoration = 'none';
    viewAllBtn.style.borderRadius = '4px';
    viewAllBtn.style.fontWeight = '500';
    viewAllBtn.style.transition = 'background-color 0.3s ease';

    // Добавляем в body
    document.body.appendChild(modal);
    document.body.style.overflow = 'hidden';

    // Применяем стили к заполнителям изображений после добавления в DOM
    document.querySelectorAll('.product-img-placeholder').forEach(placeholder => {
        placeholder.style.height = '200px';
        placeholder.style.backgroundColor = '#f5f5f5';
        placeholder.style.marginBottom = '1rem';
        placeholder.style.borderRadius = '4px';
    });

    // Показываем модальное окно с анимацией
    setTimeout(() => {
        modal.style.opacity = '1';
        modalContent.style.transform = 'translateY(0)';
    }, 10);

    // Закрываем по клику на кнопку
    closeBtn.addEventListener('click', () => {
        closeQuickView(modal);
    });

    // Закрываем по клику вне окна
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            closeQuickView(modal);
        }
    });

    // Закрываем по клавише ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            closeQuickView(modal);
        }
    });
}

/**
 * Закрывает модальное окно быстрого просмотра
 * @param {HTMLElement} modal - Элемент модального окна
 */
function closeQuickView(modal) {
    const modalContent = modal.querySelector('.quick-view-content');
    modal.style.opacity = '0';
    modalContent.style.transform = 'translateY(50px)';

    setTimeout(() => {
        document.body.removeChild(modal);
        document.body.style.overflow = '';
    }, 300);
}

// Экспорт функций для внешнего использования
window.SiteInterface = {
    init: initSiteInterface,
    showNotification: showNotification,
    showQuickView: showQuickView
};

// Автоматическая инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', initSiteInterface);