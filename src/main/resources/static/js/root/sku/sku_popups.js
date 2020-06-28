
    Vue.component('create-sku-popup', {
        props: {
            header_title: '',
            button_1_name: '',
            button_2_name: '',
        },
        template: `<div class="popup_window">
                        <div class="Dialog">
                            <div class="Dialog_overlay">
                                <div class="Dialog_content">
                                    <div class="Dialog_header">
                                        <span class="modal_header_text">{{header_title}}</span>
                                        <span><i class="material-icons" @click="closePopup">close</i></span>
                                    </div>
                                    <div class="Dialog_body">
                                        <slot></slot>
                                    </div>
                                    <div class="Dialog_footer">
                                        <div class="modal_button_1_container">                                       
                                            <button class="modal_button_1" @click="validate_form">{{button_1_name}}</button>
                                        </div>
                                        <div class="modal_button_2_container">                                       
                                            <button class="modal_button_2" @click="closePopup">{{button_2_name}}</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>`,
        methods: {
            closePopup() {

                this.$emit('close_add_popup') //просылаем событие наверх родительскому элементу
            },
            validate_form() {
                this.$emit('validate_and_add')//просылаем событие наверх родительскому элементу
            },
        },
    });

    Vue.component('update-sku-popup', {
        props: {
            header_title: '',
            button_1_name: '',
            button_2_name: '',
        },
        template: `<div class="popup_window">
                        <div class="Dialog">
                            <div class="Dialog_overlay">
                                <div class="Dialog_content">
                                    <div class="Dialog_header">
                                        <span class="modal_header_text">{{header_title}}</span>
                                        <span><i class="material-icons" @click="closePopup">close</i></span>
                                    </div>
                                    <div class="Dialog_body">
                                        <slot></slot>
                                    </div>
                                    <div class="Dialog_footer">
                                        <div class="modal_button_1_container">
                                            <button class="modal_button_1" @click="validate_form">{{button_1_name}}</button>
                                        </div>
                                        <div class="modal_button_2_container">
                                            <button class="modal_button_2" @click="closePopup">{{button_2_name}}</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>`,
        methods: {
            closePopup() {
                this.$emit('close_update_popup') //просылаем событие наверх родительскому элементу
            },
            validate_form() {
                this.$emit('validate_and_update')//просылаем событие наверх родительскому элементу
            },
        }
    });


    Vue.component('delete-sku-popup', {
        props: {
            header_title: '',
            button_1_name: '',
            button_2_name: '',
            sku_to_delete: {},
            error_message:''
        },
        template: `<div class="delete_popup_window">
    <div class="delete_dialog">
        <div class="delete_dialog_overlay">
            <div class="delete_dialog_content">
                <div class="delete_dialog_header">
                    <span class="delete_modal_header_text">{{header_title}}</span>
                    <span><i class="material-icons" @click="closePopup">close</i></span>
                </div>
                <div class="delete_dialog_body">
        <slot>
                        <span class="popup_title">Вы действительно хотите удалить карточку товара?</span>
                 <table class="popup_content_table">
                     <tr>
                         <td class="popup_left_column">Код:</td>
                         <td>{{sku_to_delete.code}}</td>
                     </tr>
                     <tr>
                         <td class="popup_left_column">Наименование:</td>
                         <td>{{sku_to_delete.name}}</td>
                     </tr>
                     <tr>
                         <td class="popup_left_column">Поставщик:</td>
                         <td>[{{sku_to_delete.contractor.contractorCode}}] {{sku_to_delete.contractor.name}}</td>
                     </tr>
                 </table>
        </slot>
            <div class="info_block">
                {{error_message}}
            </div>
                </div>
                <div class="delete_dialog_footer">
                    <div class="delete_modal_button_1_container">
                        <button class="delete_modal_button_1" @click="validate_form">{{button_1_name}}</button>
                    </div>
                    <div class="delete_modal_button_2_container">
                        <button class="delete_modal_button_2" @click="closePopup">{{button_2_name}}</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>`,
        methods: {
            closePopup() {
                this.$emit('close_delete_popup') //просылаем событие наверх родительскому элементу
            },
            validate_form() {
                this.$emit('validate_and_delete')//просылаем событие наверх родительскому элементу
            },
        }
    });
