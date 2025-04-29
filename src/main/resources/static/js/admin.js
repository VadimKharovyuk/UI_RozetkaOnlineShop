/**
 * admin.js - скрипты для административной панели
 */

document.addEventListener('DOMContentLoaded', function() {
    // Инициализация всплывающих подсказок Bootstrap
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Инициализация тостов Bootstrap
    var toastElList = [].slice.call(document.querySelectorAll('.toast'));
    var toastList = toastElList.map(function (toastEl) {
        return new bootstrap.Toast(toastEl);
    });

    // Показать все тосты при загрузке страницы
    toastList.forEach(toast => toast.show());

    // Установка заголовка страницы в верхней панели
    updatePageTitle();

    // Подсветка активного пункта меню
    highlightActiveMenuItem();

    // Инициализация мобильного меню для маленьких экранов
    initMobileSidebar();

    // Инициализация предпросмотра изображений
    initImagePreviews();

    // Инициализация подтверждений удаления
    initDeleteConfirmations();

    // Инициализация выпадающих меню выбора
    initSelectMenus();

    // Обработка форм для предотвращения двойной отправки
    preventDoubleFormSubmission();
});

/**
 * Устанавливает заголовок страницы в верхней панели
 */
function updatePageTitle() {
    const pageTitle = document.title.split(' - ')[0];
    const pageTitleElement = document.getElementById('currentPageTitle');
    if (pageTitleElement) {
        pageTitleElement.textContent = pageTitle;
    }
}

/**
 * Подсвечивает активный пункт меню в боковой панели
 */
function highlightActiveMenuItem() {
    // Получаем текущий путь URL
    const currentPath = window.location.pathname;

    // Находим все ссылки в боковом меню
    const navLinks = document.querySelectorAll('.admin-sidebar .nav-link');

    // Убираем активный класс со всех ссылок
    navLinks.forEach(link => {
        link.classList.remove('active');
    });

    // Проходим по всем ссылкам и находим соответствующую текущему пути
    navLinks.forEach(link => {
        const href = link.getAttribute('href');

        // Если ссылка совпадает с текущим путем
        if (href === currentPath) {
            link.classList.add('active');
            return;
        }

        // Если текущий путь начинается с href (подраздел)
        if (href !== '/' && currentPath.startsWith(href) && href !== '/admin') {
            link.classList.add('active');
        }

        // Специальная обработка для главной страницы админки
        if (href === '/admin' && (currentPath === '/admin' || currentPath === '/admin/')) {
            link.classList.add('active');
        }
    });
}

/**
 * Инициализирует мобильное боковое меню
 */
function initMobileSidebar() {
    // Добавляем кнопку-гамбургер, если её еще нет
    if (!document.querySelector('.sidebar-toggle')) {
        const header = document.querySelector('.admin-header');
        if (header) {
            const toggleButton = document.createElement('button');
            toggleButton.className = 'btn btn-link sidebar-toggle d-lg-none me-3';
            toggleButton.innerHTML = '<i class="fas fa-bars"></i>';
            header.querySelector('.d-flex').prepend(toggleButton);

            // Обработчик клика по кнопке
            toggleButton.addEventListener('click', function() {
                const sidebar = document.querySelector('.admin-sidebar');
                if (sidebar) {
                    sidebar.classList.toggle('show');
                }
            });

            // Закрытие меню при клике вне его
            document.addEventListener('click', function(event) {
                const sidebar = document.querySelector('.admin-sidebar');
                const toggle = document.querySelector('.sidebar-toggle');

                if (sidebar && sidebar.classList.contains('show') &&
                    !sidebar.contains(event.target) &&
                    !toggle.contains(event.target)) {
                    sidebar.classList.remove('show');
                }
            });
        }
    }
}

/**
 * Инициализирует предпросмотр изображений для загрузки файлов
 */
function initImagePreviews() {
    // Находим все поля загрузки изображений, у которых есть data-preview атрибут
    const imageInputs = document.querySelectorAll('input[type="file"][accept*="image"][data-preview]');

    imageInputs.forEach(input => {
        const previewId = input.getAttribute('data-preview');
        const previewElement = document.getElementById(previewId);

        if (previewElement) {
            input.addEventListener('change', function() {
                if (this.files && this.files[0]) {
                    const reader = new FileReader();

                    reader.onload = function(e) {
                        // Если у превью есть тег img внутри
                        const previewImg = previewElement.querySelector('img');
                        if (previewImg) {
                            previewImg.src = e.target.result;
                        } else {
                            // Иначе используем сам элемент (если это например div с фоном)
                            previewElement.style.backgroundImage = `url('${e.target.result}')`;
                        }

                        // Показываем превью
                        previewElement.classList.remove('d-none');
                    };

                    reader.readAsDataURL(this.files[0]);
                } else {
                    // Если файл не выбран, скрываем превью
                    previewElement.classList.add('d-none');
                }
            });
        }
    });

    // Также обрабатываем все элементы с data-input-id
    const previewElements = document.querySelectorAll('[data-input-id]');

    previewElements.forEach(preview => {
        const inputId = preview.getAttribute('data-input-id');
        const input = document.getElementById(inputId);

        if (input && input.type === 'file') {
            // Если у поля есть класс 'has-preview', значит уже есть предпросмотр
            if (preview.classList.contains('has-preview')) {
                // Добавляем кнопку удаления, если её еще нет
                if (!preview.querySelector('.preview-remove')) {
                    const removeButton = document.createElement('button');
                    removeButton.className = 'btn btn-sm btn-danger position-absolute top-0 end-0 m-2 preview-remove';
                    removeButton.innerHTML = '<i class="fas fa-times"></i>';
                    preview.appendChild(removeButton);

                    // Обработчик удаления изображения
                    removeButton.addEventListener('click', function(e) {
                        e.preventDefault();
                        // Очищаем input
                        input.value = '';
                        // Скрываем превью или очищаем его
                        preview.classList.add('d-none');
                        const previewImg = preview.querySelector('img');
                        if (previewImg) {
                            previewImg.src = '';
                        }
                    });
                }
            }
        }
    });
}

/**
 * Инициализирует подтверждения удаления для всех форм/кнопок с data-confirm атрибутом
 */
function initDeleteConfirmations() {
    // Обрабатываем все элементы с атрибутом data-confirm
    const confirmElements = document.querySelectorAll('[data-confirm]');

    confirmElements.forEach(element => {
        const message = element.getAttribute('data-confirm') || 'Вы уверены, что хотите выполнить это действие?';

        element.addEventListener('click', function(e) {
            if (!confirm(message)) {
                e.preventDefault();
            }
        });
    });
}

/**
 * Инициализирует выпадающие меню выбора с поиском, если есть библиотека Select2
 */
function initSelectMenus() {
    // Проверяем, доступен ли Select2
    if (typeof $.fn.select2 !== 'undefined') {
        // Инициализируем Select2 для элементов с классом 'select2'
        $('.select2').select2({
            language: 'ru',
            width: '100%'
        });

        // Инициализируем с опцией Ajax для элементов с data-ajax-url
        $('select[data-ajax-url]').each(function() {
            const ajaxUrl = $(this).data('ajax-url');
            const placeholder = $(this).data('placeholder') || 'Выберите значение';

            $(this).select2({
                language: 'ru',
                width: '100%',
                placeholder: placeholder,
                allowClear: true,
                ajax: {
                    url: ajaxUrl,
                    dataType: 'json',
                    delay: 250,
                    data: function(params) {
                        return {
                            q: params.term,
                            page: params.page || 1
                        };
                    },
                    processResults: function(data, params) {
                        params.page = params.page || 1;

                        return {
                            results: data.items,
                            pagination: {
                                more: (params.page * 10) < data.total_count
                            }
                        };
                    },
                    cache: true
                },
                minimumInputLength: 1
            });
        });
    }
}

/**
 * Предотвращает повторную отправку форм
 */
function preventDoubleFormSubmission() {
    const forms = document.querySelectorAll('form:not([data-no-prevent-double])');

    forms.forEach(form => {
        form.addEventListener('submit', function() {
            // Находим все кнопки отправки формы
            const submitButtons = form.querySelectorAll('button[type="submit"], input[type="submit"]');

            submitButtons.forEach(button => {
                // Сохраняем оригинальный текст кнопки
                const originalText = button.innerHTML;

                // Отключаем кнопку и меняем текст
                button.disabled = true;
                if (!button.classList.contains('no-loading-text')) {
                    button.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span> Обработка...';
                }

                // Восстанавливаем состояние кнопки через 5 секунд, если форма не отправилась
                setTimeout(() => {
                    button.disabled = false;
                    if (!button.classList.contains('no-loading-text')) {
                        button.innerHTML = originalText;
                    }
                }, 5000);
            });
        });
    });
}

/**
 * Форматирует число как валюту
 * @param {number} value - Число для форматирования
 * @param {string} currency - Код валюты (UAH, USD и т.д.)
 * @returns {string} - Отформатированная строка
 */
function formatCurrency(value, currency = 'UAH') {
    const formatter = new Intl.NumberFormat('uk-UA', {
        style: 'currency',
        currency: currency,
        minimumFractionDigits: 2
    });

    return formatter.format(value);
}

/**
 * Форматирует дату в локальный формат
 * @param {string} dateString - Строка с датой
 * @param {boolean} includeTime - Включать ли время
 * @returns {string} - Отформатированная дата
 */
function formatDate(dateString, includeTime = false) {
    const options = {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    };

    if (includeTime) {
        options.hour = '2-digit';
        options.minute = '2-digit';
    }

    const date = new Date(dateString);
    return date.toLocaleDateString('uk-UA', options);
}