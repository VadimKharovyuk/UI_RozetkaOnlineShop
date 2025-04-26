/**
 * Основной скрипт для управления интерфейсом сайта
 *
 * Функциональность:
 * - Боковое меню (открытие/закрытие/подменю)
 * - Поисковая строка на мобильных устройствах
 * - Обработка изменения размера окна
 */

// Публичный объект для доступа к функциям модуля
window.SiteInterface = {};

/**
 * Инициализация всех компонентов интерфейса сайта
 */
SiteInterface.init = function() {
    // Инициализация бокового меню
    SiteInterface.initSidebar();

    // Инициализация поиска на мобильных устройствах
    SiteInterface.initMobileSearch();

    // Обработка изменения размера окна
    window.addEventListener('resize', SiteInterface.handleResize);

    // Инициализация действий с карточками, если модуль доступен
    if (window.CardActions && typeof window.CardActions.init === 'function') {
        window.CardActions.init();
    }
};

/**
 * Инициализация бокового меню
 */
SiteInterface.initSidebar = function() {
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
};

/**
 * Инициализация поиска на мобильных устройствах
 */
SiteInterface.initMobileSearch = function() {
    const searchToggle = document.getElementById('searchToggle');
    const searchContainer = document.getElementById('searchContainer');

    if (!searchToggle || !searchContainer) {
        return;
    }

    searchToggle.addEventListener('click', () => {
        searchContainer.classList.toggle('active');
    });
};

/**
 * Обработка изменения размера окна
 */
SiteInterface.handleResize = function() {
    const searchContainer = document.getElementById('searchContainer');
    if (searchContainer && window.innerWidth > 768) {
        searchContainer.classList.remove('active');
    }
};

// Автоматическая инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    SiteInterface.init();
});