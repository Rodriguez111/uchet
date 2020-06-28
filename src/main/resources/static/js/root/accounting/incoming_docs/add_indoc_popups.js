    Vue.component('warning-popup', {
        props: {
            header_title: '',
            button_1_name: '',
            button_2_name: '',
        },
        template: `<div class="warning_popup_window">
    <div class="warning_dialog">
        <div class="warning_dialog_overlay">
            <div class="warning_dialog_content">
                <div class="warning_dialog_header">
                    <span class="warning_modal_header_text">{{header_title}}</span>
                    <span><i class="material-icons" @click="cancelPopup">close</i></span>
                </div>
                <div class="warning_dialog_body">
        <slot>
                        

        </slot>
                </div>
                <div class="warning_dialog_footer">
                    <div class="warning_modal_button_1_container">
                        <button class="warning_modal_button_1" @click="confirm">{{button_1_name}}</button>
                    </div>
                    <div class="warning_modal_button_2_container">
                        <button class="warning_modal_button_2" @click="cancelPopup">{{button_2_name}}</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>`,
        methods: {
            cancelPopup() {
                this.$emit('warning_result', false) //посылаем событие наверх родительскому элементу
            },
            confirm() {
                this.$emit('warning_result', true)//посылаем событие наверх родительскому элементу
            },
        }
    });
