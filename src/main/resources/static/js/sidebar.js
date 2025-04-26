// sidebar.js - Функции для работы с боковым меню

document.addEventListener('DOMContentLoaded', function() {
    // DOM элементы бокового меню
    const menuButton = document.getElementById('menuButton');
    const sidebar = document.getElementById('sidebar');
    const closeSidebar = document.getElementById('closeSidebar');
    const overlay = document.getElementById('overlay');
    const menuItems = document.querySelectorAll('.menu-item');

    // Открытие бокового меню
    menuButton.addEventListener('click', () => {
        sidebar.classList.add('active');
        overlay.classList.add('active');
        document.body.style.overflow = 'hidden'; // Запрет прокрутки при открытом меню
    });

    // Закрытие бокового меню
    function closeSidebarMenu() {
        sidebar.classList.remove('active');
        overlay.classList.remove('active');
        document.body.style.overflow = ''; // Возвращаем прокрутку
    }

    // Обработчики для закрытия
    closeSidebar.addEventListener('click', closeSidebarMenu);
    overlay.addEventListener('click', closeSidebarMenu);

    // Управление подменю
    menuItems.forEach(item => {
        const toggle = item.querySelector('.menu-toggle');
        if (toggle) {
            item.addEventListener('click', () => {
                const submenu = item.nextElementSibling;
                if (submenu && submenu.classList.contains('submenu')) {
                    // Закрытие всех других подменю
                    document.querySelectorAll('.submenu.active').forEach(menu => {
                        if (menu !== submenu) {
                            menu.classList.remove('active');
                            const toggleIcon = menu.previousElementSibling.querySelector('.menu-toggle');
                            if (toggleIcon) toggleIcon.classList.remove('active');
                        }
                    });

                    // Переключение текущего подменю
                    submenu.classList.toggle('active');
                    toggle.classList.toggle('active');
                }
            });
        }
    });
});