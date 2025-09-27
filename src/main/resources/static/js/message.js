/**
 * 消息提示工具类
 * 提供统一的成功/错误消息显示功能
 */

class MessageUtil {
    /**
     * 显示成功消息
     * @param {string} message - 消息内容
     * @param {number} duration - 显示时长（毫秒），默认3000ms
     */
    static showSuccess(message, duration = 3000) {
        this.showMessage(message, 'success', duration);
    }

    /**
     * 显示错误消息
     * @param {string} message - 消息内容
     * @param {number} duration - 显示时长（毫秒），默认3000ms
     */
    static showError(message, duration = 3000) {
        this.showMessage(message, 'danger', duration);
    }

    /**
     * 显示警告消息
     * @param {string} message - 消息内容
     * @param {number} duration - 显示时长（毫秒），默认3000ms
     */
    static showWarning(message, duration = 3000) {
        this.showMessage(message, 'warning', duration);
    }

    /**
     * 显示信息消息
     * @param {string} message - 消息内容
     * @param {number} duration - 显示时长（毫秒），默认3000ms
     */
    static showInfo(message, duration = 3000) {
        this.showMessage(message, 'info', duration);
    }

    /**
     * 显示消息的通用方法
     * @param {string} message - 消息内容
     * @param {string} type - 消息类型（success, danger, warning, info）
     * @param {number} duration - 显示时长（毫秒）
     */
    static showMessage(message, type, duration) {
        // 移除已存在的消息
        const existingAlerts = document.querySelectorAll('.alert.position-fixed');
        existingAlerts.forEach(alert => {
            alert.remove();
        });

        // 创建新的消息元素
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        alertDiv.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px; max-width: 500px;';
        alertDiv.role = 'alert';

        // 根据类型设置图标
        let iconClass = '';
        switch (type) {
            case 'success':
                iconClass = 'fa-check-circle';
                break;
            case 'danger':
                iconClass = 'fa-exclamation-circle';
                break;
            case 'warning':
                iconClass = 'fa-exclamation-triangle';
                break;
            case 'info':
                iconClass = 'fa-info-circle';
                break;
            default:
                iconClass = 'fa-info-circle';
        }

        alertDiv.innerHTML = `
            <i class="fas ${iconClass} me-2"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        `;

        // 添加到页面
        document.body.appendChild(alertDiv);

        // 自动消失
        if (duration > 0) {
            setTimeout(() => {
                if (alertDiv.parentNode) {
                    const bsAlert = bootstrap.Alert.getOrCreateInstance(alertDiv);
                    bsAlert.close();
                }
            }, duration);
        }
    }

    /**
     * 显示确认对话框
     * @param {string} message - 确认消息
     * @param {function} onConfirm - 确认回调函数
     * @param {function} onCancel - 取消回调函数
     */
    static showConfirm(message, onConfirm, onCancel) {
        // 创建模态框元素
        const modalId = 'confirmModal_' + Date.now();
        const modalHtml = `
            <div class="modal fade" id="${modalId}" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">确认操作</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            ${message}
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" id="confirmBtn">确认</button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        // 添加到页面
        const modalContainer = document.createElement('div');
        modalContainer.innerHTML = modalHtml;
        document.body.appendChild(modalContainer);

        // 显示模态框
        const modal = new bootstrap.Modal(document.getElementById(modalId));
        modal.show();

        // 绑定确认按钮事件
        document.getElementById('confirmBtn').addEventListener('click', function() {
            if (onConfirm && typeof onConfirm === 'function') {
                onConfirm();
            }
            modal.hide();
        });

        // 绑定取消事件
        document.querySelector(`#${modalId} .btn-secondary`).addEventListener('click', function() {
            if (onCancel && typeof onCancel === 'function') {
                onCancel();
            }
        });

        // 模态框隐藏后清理DOM
        document.getElementById(modalId).addEventListener('hidden.bs.modal', function() {
            document.body.removeChild(modalContainer);
        });
    }
}

// 导出为全局变量
window.MessageUtil = MessageUtil;