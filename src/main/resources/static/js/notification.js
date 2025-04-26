// notification.js - Функции для работы с уведомлениями и модальными окнами

document.addEventListener('DOMContentLoaded', function() {
    // Обработчики для кнопок категорий
    document.querySelectorAll('.category-card .action-icon').forEach(button => {
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
                // Функция быстрого просмотра
                const categoryName = button.closest('.category-card').querySelector('.category-title').textContent;
                showQuickView(categoryName);
            }
        });
    });
});

// Функция для отображения модального окна быстрого просмотра
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

    // Добавляем примеры товаров (заглушки)
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

    document.querySelectorAll('.product-img-placeholder').forEach(placeholder => {
        placeholder.style.height = '200px';
        placeholder.style.backgroundColor = '#f5f5f5';
        placeholder.style.marginBottom = '1rem';
        placeholder.style.borderRadius = '4px';
    });

    viewAllBtn.style.display = 'inline-block';
    viewAllBtn.style.padding = '0.8rem 1.5rem';
    viewAllBtn.style.backgroundColor = '#1a1a1a';
    viewAllBtn.style.color = '#fff';
    viewAllBtn.style.textDecoration = 'none';
    viewAllBtn.style.borderRadius = '4px';
    viewAllBtn.style.fontWeight = '500';
    viewAllBtn.style.transition = 'background-color 0.3s ease';

    // Добавляем на страницу
    document.body.appendChild(modal);
    document.body.style.overflow = 'hidden';

    // Показываем модальное окно с анимацией
    setTimeout(() => {
        modal.style.opacity = '1';
        modalContent.style.transform = 'translateY(0)';
    }, 10);

    // Закрытие по клику на кнопку
    closeBtn.addEventListener('click', () => {
        closeQuickView(modal);
    });

    // Закрытие по клику вне контента
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            closeQuickView(modal);
        }
    });

    // Закрытие по клавише ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            closeQuickView(modal);
        }
    });
}

// Функция закрытия модального окна
function closeQuickView(modal) {
    const modalContent = modal.querySelector('.quick-view-content');
    modal.style.opacity = '0';
    modalContent.style.transform = 'translateY(50px)';

    setTimeout(() => {
        document.body.removeChild(modal);
        document.body.style.overflow = '';
    }, 300);
}