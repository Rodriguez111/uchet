/*
1. Список названий всех чекбоксов хранится в переменной-массиве permissionNames. На нулевой позиции - корневой пункт "Учет", остальные - подпозиции.
2. Видимость всех чекбоксов кроме главного зависит от тот, отмечен ли главный (<div class="grid-item" v-if="box0IsChecked">)
    3. Видимость  каждого чекбокса зависит от состояния своей переменной box2IsChecked, box3IsChecked и т. д.)
4. Состояние чекбокса меняется в зависимости от: @change="box2IsChecked=!box2IsChecked"
5. При загрузке элемента с чекбоксами проверяется, присутствует ли в списке разрешений хотя бы одно, кроме корневого (хотябы одно кроме учета).
Если да, то все чекбоксы отображаются, если нет, то отображается только главный чекбокс "Учет":
created() {
    this.box0IsChecked = this.checkIfAnyPermissionsExists();
},

6.Вотчер за главным чекбоксом ( watch: box0IsChecked) наблюдает:
- если его кликнули и (box0IsChecked = true и присутствует хотя бы одно разрешение, кроме корневого),
    то проверяет все разрешения и выставляет переменные отображения каждого чекбокса в зависимости от того, есть ли разрешение или нет.
- иначе проверяет (box0IsChecked = true и в данный момент нет ни одного разрешения) ,
тогда выставляет все переменные отвечающие за остальные чекбоксы в положение true,
а наблюдатели за теми переменными в свою очередь уже присваивают соответствующие разрешения.
Так же добавляет разрешение для себя самого (Позиция Учет)
- иначе, если box0IsChecked = false, то есть если главный чекбокс отжат,
то выставляет все переменные отвечающие за остальные чекбоксы в положение false,
а наблюдатели за теми переменными в свою очередь уже удаляют соответствующие разрешения.
Так же удаляет разрешение для себя самого (Позиция Учет)
7. Функция switchPermission(permission, turnOn) присваивает разрешение,
если передано значение turnOn = true, и удаляет, если turnOn = false
Так же, функция вызывает checkIfNoMorePermissionsRemained(permission), которая проверяет,
осталось ли хоть одно разрешение кроме главного (кроме Учет), и если нет,
то переключает переменную главного чекбокса в положение false, чтобы скрыть остальные.

 *Функция checkIfAnyPermissionsExists() проверяет, присутствует ли хотя бы одно разрешение,
 кроме Учет (поэтому итерация начинается с i=1, то есть пропускаем нулевую позицию Учет)
*/












Vue.component('create-position-popup', {
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

Vue.component('update-position-popup', {
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


Vue.component('delete-position-popup', {
    props: {
        header_title: '',
        button_1_name: '',
        button_2_name: '',
        position_to_delete: {},
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
                     <span class="popup_title">Вы действительно хотите удалить эту должность?</span>
                 <table class="popup_content_table">
                     <tr>
                         <td class="popup_left_column">Должность:</td>
                         <td>{{position_to_delete.position}}</td>
                     </tr>
                     <tr>
                         <td class="popup_left_column">Описание:</td>
                         <td>{{position_to_delete.positionDescription}}</td>
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
            this.$emit('close_delete_popup') //посылаем событие наверх родительскому элементу
        },
        validate_form() {
            this.$emit('validate_and_delete')//посылаем событие наверх родительскому элементу
        },
    }
});


Vue.component('checkbox-panel-component', {
    props: {
        permissions: [],
    },
    template: `
     <div class="checkbox_panel">
                                <div class="checkbox_panel_title"><h3>Доступы</h3></div>

                                <div class="checkbox_panel_body">
                                    <div class="checkbox_panel_tabs">
                                        <button class="checkbox_panel_tab"  :class="{'checkbox_panel_tab_active':accountingIsVisible}" @click="toggleTab('Accounting')">Учет</button>
                                        <button class="checkbox_panel_tab"  :class="{'checkbox_panel_tab_active':skuIsVisible}" @click="toggleTab('Sku')">Товары</button>
                                        <button class="checkbox_panel_tab"  :class="{'checkbox_panel_tab_active':contractorsIsVisible}" @click="toggleTab('Contractors')">Контрагенты</button>                                     
                                        <button class="checkbox_panel_tab" :class="{'checkbox_panel_tab_active':adminIsVisible}" @click="toggleTab('Admin')">Администрирование</button>                                        
                                    </div>
                                    
                                    <accounting-tab-component v-if="accountingIsVisible"></accounting-tab-component> 
                                    <sku-tab-component v-if="skuIsVisible"></sku-tab-component>  
                                    <contractors-tab-component v-if="contractorsIsVisible"></contractors-tab-component>                                     
                                    <admin-tab-component v-if="adminIsVisible"></admin-tab-component>  
                                                                                                    
                                </div>
     </div>
                            
    
    
    `,
    data() {
        return {
            accountingIsVisible: true,
            skuIsVisible: false,
            contractorsIsVisible: false,
            customersIsVisible: false,
            adminIsVisible: false,
        }
    },
    methods: {
        toggleTab(tabName) {
            if (tabName === "Accounting") {
                this.accountingIsVisible = true;
                this.skuIsVisible = false;
                this.contractorsIsVisible = false;
                this.adminIsVisible = false;
            } else if (tabName === "Sku") {
                this.accountingIsVisible = false;
                this.skuIsVisible = true;
                this.contractorsIsVisible = false;
                this.adminIsVisible = false;
            } else if (tabName === "Contractors") {
                this.accountingIsVisible = false;
                this.skuIsVisible = false;
                this.contractorsIsVisible = true;
                this.adminIsVisible = false;
            } else if (tabName === "Admin") {
                this.accountingIsVisible = false;
                this.skuIsVisible = false;
                this.contractorsIsVisible = false;
                this.adminIsVisible = true;
            }


        },

    },
});

Vue.component('accounting-tab-component', {
    template: `
    <div id="Accounting" class="checkbox_panel_tabcontent">
         <div class="four-col-grid">
             <div class="grid-item">
                 <div class="form_group">
                     <input type="checkbox" :checked="box0IsChecked"  @change="box0IsChecked=!box0IsChecked">
                     <br>
                     <label class="form_label">Учет:</label>
                 </div>
             </div>        
             <div class="grid-item" v-if="box0IsChecked">
                 <div class="form_group">
                     <input type="checkbox" :checked="box1IsChecked"  @change="box1IsChecked=!box1IsChecked">
                     <br>
                     <label class="form_label">Остатки товаров:</label>
                 </div>
             </div>
             <div class="grid-item" v-if="box0IsChecked">
                 <div class="form_group">
                     <input type="checkbox" :checked="box2IsChecked"  @change="box2IsChecked=!box2IsChecked">
                     <br>
                     <label class="form_label">Приходные документы:</label>
                 </div>
             </div>
             <div class="grid-item" v-if="box0IsChecked">
                 <div class="form_group">
                     <input type="checkbox" :checked="box3IsChecked"  @change="box3IsChecked=!box3IsChecked">
                     <br>
                     <label class="form_label">Приход товаров:</label>
                 </div>
             </div>
             <div class="grid-item" v-if="box0IsChecked">
                 <div class="form_group">
                     <input type="checkbox" :checked="box4IsChecked" @change="box4IsChecked=!box4IsChecked">
                     <br>
                     <label class="form_label">Расходные документы:</label>
                 </div>
             </div>
             <div class="grid-item" v-if="box0IsChecked">
                 <div class="form_group">
                     <input type="checkbox" :checked="box5IsChecked" @change="box5IsChecked=!box5IsChecked">
                     <br>
                     <label class="form_label">Расход товаров:</label>
                 </div>
             </div>                                           
         </div>
         </div>
    `,
    data() {
        return {
            permissionNames:[
                'Учет',
                'Остатки товаров',
                'Приходные документы',
                'Приход товаров',
                'Расходные документы',
                'Расход товаров',
            ],
            box0IsChecked:false,
            box1IsChecked:false,
            box2IsChecked:false,
            box3IsChecked:false,
            box4IsChecked:false,
            box5IsChecked:false,
        }
    },
    created() {
        this.box0IsChecked = this.checkIfAnyPermissionsExists();
    },
    methods: {
        switchAllPermissions(onOff) {
            for (var i = 0; i < this.permissionNames.length; i++) {
                this.switchPermission(this.permissionNames[i], onOff);
            }
        },
        switchPermission(permission, turnOn) {
            var newPermission = {
                permissionDescription: permission
            };
            if (turnOn) {
                if (!_.find(positionsApp.newObject.permissions, newPermission)) {
                    positionsApp.newObject.permissions.push(newPermission);
                }
            } else {
                _.remove(positionsApp.newObject.permissions, function(el) { return el.permissionDescription === permission;});
            }
            this.checkIfNoMorePermissionsRemained(permission);
        },
        checkIfNoMorePermissionsRemained(permission) {
            if (!this.checkIfAnyPermissionsExists() && permission !== this.permissionNames[0]) {
                this.switchPermission(this.permissionNames[0], false);
                this.box0IsChecked = false;
            }
        },
        checkIfExists(permission) {
            var result = false;
            var newPermission = {
                permissionDescription: permission
            };
            if (_.find(positionsApp.newObject.permissions, newPermission)) {
                result = true;
            }
            return result;
        },
        checkIfAnyPermissionsExists() {
            var result = false;
            for (var i = 1; i < this.permissionNames.length; i++) {
                var newPermission = {
                    permissionDescription: this.permissionNames[i]
                };
                if (_.find(positionsApp.newObject.permissions, newPermission)) {
                    result = true;
                    break;
                }
            }
            return result;
        }
    },
    watch: {
        box0IsChecked: function () {
            if (this.box0IsChecked && this.checkIfAnyPermissionsExists()) {
                this.box1IsChecked = this.checkIfExists(this.permissionNames[1]);
                this.box2IsChecked = this.checkIfExists(this.permissionNames[2]);
                this.box3IsChecked = this.checkIfExists(this.permissionNames[3]);
                this.box4IsChecked = this.checkIfExists(this.permissionNames[4]);
                this.box5IsChecked = this.checkIfExists(this.permissionNames[5]);
            } else if (this.box0IsChecked && !this.checkIfAnyPermissionsExists()) {
                this.box1IsChecked = true;
                this.box2IsChecked = true;
                this.box3IsChecked = true;
                this.box4IsChecked = true;
                this.box5IsChecked = true;
                this.switchPermission(this.permissionNames[0], true);
            }else if (!this.box0IsChecked) {
                this.box1IsChecked = false;
                this.box2IsChecked = false;
                this.box3IsChecked = false;
                this.box4IsChecked = false;
                this.box5IsChecked = false;
                this.switchPermission(this.permissionNames[0], false);
            }
        },
        box1IsChecked: function () {
            if (this.box1IsChecked) {
                this.switchPermission(this.permissionNames[1], true);
            } else {
                this.switchPermission(this.permissionNames[1], false);
            }
        },
        box2IsChecked: function () {
            if (this.box2IsChecked) {
                this.switchPermission(this.permissionNames[2], true);
            } else {
                this.switchPermission(this.permissionNames[2], false);
            }
        },
        box3IsChecked: function () {
            if (this.box3IsChecked) {
                this.switchPermission(this.permissionNames[3], true);
            } else {
                this.switchPermission(this.permissionNames[3], false);
            }
        },
        box4IsChecked: function () {
            if (this.box4IsChecked) {
                this.switchPermission(this.permissionNames[4], true);
            } else {
                this.switchPermission(this.permissionNames[4], false);
            }
        },
        box5IsChecked: function () {
            if (this.box5IsChecked) {
                this.switchPermission(this.permissionNames[5], true);
            } else {
                this.switchPermission(this.permissionNames[5], false);
            }
        },

    }
});

Vue.component('sku-tab-component', {
    template: `
    <div id="Accounting" class="checkbox_panel_tabcontent">
                                        <div class="four-col-grid">        
                                            <div class="grid-item">
                                                <div class="form_group">
                                                    <input type="checkbox" :checked="box0IsChecked"  @change="box0IsChecked=!box0IsChecked">
                                                    <br>
                                                    <label class="form_label">Товары:</label>
                                                </div>
                                            </div>                                                                                    
                                        </div>
                                        </div>
    `,
    data() {
        return {
            permissionNames:[
                'Товары',
            ],
            box0IsChecked:this.checkIfExists('Товары'),
        }
    },
    methods: {
        switchAllPermissions(onOff) {
            for (var i = 0; i < this.permissionNames.length; i++) {
                this.switchPermission(this.permissionNames[i], onOff);
            }
        },
        switchPermission(permission, turnOn) {
            var newPermission = {
                permissionDescription: permission
            };
            if (turnOn) {
                if (!_.find(positionsApp.newObject.permissions, newPermission)) {
                    positionsApp.newObject.permissions.push(newPermission);
                }
            } else {
                _.remove(positionsApp.newObject.permissions, function(el) { return el.permissionDescription === permission;});
            }
        },

        checkIfExists(permission) {
            var result = false;
            var newPermission = {
                permissionDescription: permission
            };
            if (_.find(positionsApp.newObject.permissions, newPermission)) {
                result = true;
            }
            return result;
        },
    },
    watch: {
        box0IsChecked: function () {
            if (this.box0IsChecked) {
                this.switchPermission(this.permissionNames[0], true);
            }else {
                this.switchPermission(this.permissionNames[0], false);
            }
        },


    }
});

Vue.component('contractors-tab-component', {
    template: `
    <div id="Contractors" class="checkbox_panel_tabcontent">
                                        <div class="four-col-grid">        
                                            <div class="grid-item">
                                                <div class="form_group">
                                                    <input type="checkbox" :checked="box0IsChecked"  @change="box0IsChecked=!box0IsChecked">
                                                    <br>
                                                    <label class="form_label">Поставщики:</label>
                                                </div>
                                            </div>                                                                                    
                                        </div>
                                        </div>
    `,
    data() {
        return {
            permissionNames:[
                'Контрагенты',
            ],
            box0IsChecked:this.checkIfExists('Контрагенты'),
        }
    },
    methods: {
        switchAllPermissions(onOff) {
            for (var i = 0; i < this.permissionNames.length; i++) {
                this.switchPermission(this.permissionNames[i], onOff);
            }
        },
        switchPermission(permission, turnOn) {
            var newPermission = {
                permissionDescription: permission
            };
            if (turnOn) {
                if (!_.find(positionsApp.newObject.permissions, newPermission)) {
                    positionsApp.newObject.permissions.push(newPermission);
                }
            } else {
                _.remove(positionsApp.newObject.permissions, function(el) { return el.permissionDescription === permission;});
            }
        },

        checkIfExists(permission) {
            var result = false;
            var newPermission = {
                permissionDescription: permission
            };
            if (_.find(positionsApp.newObject.permissions, newPermission)) {
                result = true;
            }
            return result;
        },
    },
    watch: {
        box0IsChecked: function () {
            if (this.box0IsChecked) {
                this.switchPermission(this.permissionNames[0], true);
            }else {
                this.switchPermission(this.permissionNames[0], false);
            }
        },


    }
})

Vue.component('admin-tab-component', {
    template: `
    <div id="Admin" class="checkbox_panel_tabcontent">
                                        <div class="four-col-grid">        
                                            <div class="grid-item">
                                                <div class="form_group">
                                                    <input type="checkbox" :checked="box0IsChecked"  @change="box0IsChecked=!box0IsChecked">
                                                    <br>
                                                    <label class="form_label">Администрирование:</label>
                                                </div>
                                            </div> 
                                            <div class="grid-item" v-if="box0IsChecked">
                                                <div class="form_group">
                                                    <input type="checkbox" :checked="box1IsChecked"  @change="box1IsChecked=!box1IsChecked">
                                                    <br>
                                                    <label class="form_label">Пользователи:</label>
                                                </div>
                                            </div> 
                                            <div class="grid-item" v-if="box0IsChecked">
                                                <div class="form_group">
                                                    <input type="checkbox" :checked="box2IsChecked"  @change="box2IsChecked=!box2IsChecked">
                                                    <br>
                                                    <label class="form_label">Должности:</label>
                                                </div>
                                            </div>                                                                                                                                 
                                        </div>
                                        </div>
    `,
    data() {
        return {
            permissionNames:[
                'Администрирование',
                'Пользователи',
                'Должности',
            ],
            box0IsChecked:false,
            box1IsChecked:false,
            box2IsChecked:false,
        }
    },
    created() {
        this.box0IsChecked = this.checkIfAnyPermissionsExists();
    },
    methods: {
        switchAllPermissions(onOff) {
            for (var i = 0; i < this.permissionNames.length; i++) {
                this.switchPermission(this.permissionNames[i], onOff);
            }
        },
        switchPermission(permission, turnOn) {
            var newPermission = {
                permissionDescription: permission
            };
            if (turnOn) {
                if (!_.find(positionsApp.newObject.permissions, newPermission)) {
                    positionsApp.newObject.permissions.push(newPermission);
                }
            } else {
                _.remove(positionsApp.newObject.permissions, function(el) { return el.permissionDescription === permission;});
            }
            this.checkIfNoMorePermissionsRemained(permission);
        },
        checkIfNoMorePermissionsRemained(permission) {
            if (!this.checkIfAnyPermissionsExists() && permission !== this.permissionNames[0]) {
                this.switchPermission(this.permissionNames[0], false);
                this.box0IsChecked = false;
            }
        },
        checkIfExists(permission) {
            var result = false;
            var newPermission = {
                permissionDescription: permission
            };
            if (_.find(positionsApp.newObject.permissions, newPermission)) {
                result = true;
            }
            return result;
        },
        checkIfAnyPermissionsExists() {
            var result = false;
            for (var i = 1; i < this.permissionNames.length; i++) {
                var newPermission = {
                    permissionDescription: this.permissionNames[i]
                };
                if (_.find(positionsApp.newObject.permissions, newPermission)) {
                    result = true;
                    break;
                }
            }
            return result;
        }
    },
    watch: {
        box0IsChecked: function () {
            if (this.box0IsChecked && this.checkIfAnyPermissionsExists()) {
                this.box1IsChecked = this.checkIfExists(this.permissionNames[1]);
                this.box2IsChecked = this.checkIfExists(this.permissionNames[2]);
            } else if (this.box0IsChecked && !this.checkIfAnyPermissionsExists()) {
                this.box1IsChecked = true;
                this.box2IsChecked = true;
                this.switchPermission(this.permissionNames[0], true);
            }else if (!this.box0IsChecked) {
                this.box1IsChecked = false;
                this.box2IsChecked = false;
                this.switchPermission(this.permissionNames[0], false);
            }
        },
        box1IsChecked: function () {
            if (this.box1IsChecked) {
                this.switchPermission(this.permissionNames[1], true);
            } else {
                this.switchPermission(this.permissionNames[1], false);
            }
        },
        box2IsChecked: function () {
            if (this.box2IsChecked) {
                this.switchPermission(this.permissionNames[2], true);
            } else {
                this.switchPermission(this.permissionNames[2], false);
            }
        },
    }

});





