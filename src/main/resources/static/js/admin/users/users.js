let usersControllerGetAll = Vue.resource('./users_controller/all{/id}');
let usersControllerGetFiltered = Vue.resource('./users_controller/local_filter');
let usersController = Vue.resource('./users_controller{/id}');
let positionsControllerApi = Vue.resource('./positions_controller/all{/id}');

let appUsers = Vue.component('app-users', {
    props: [
        'listOfUsers',
        'tableUpdateToggle'
    ],
    template: `<div>
        <tr class="main_table_tr" v-for="user in listOfUsers">
        <td class="main_table_td column_1">{{user.login}}</td>
        <td class="main_table_td column_2">{{user.fullName}}</td>
        <td class="main_table_td column_3">{{user.position.position}}</td>
        <td class="main_table_td column_4">{{user.active ? "Да" : "Нет"}} </td>
        <td class="main_table_td column_5">{{user.createDate}}</td>
        <td class="main_table_td column_6">{{user.lastUpdateDate}}</td>
        <td class="main_table_td column_7"><button @click="showUpdatePopup(user.id)">Изменить</button></td>
        <td class="main_table_td column_8"><button @click="showDeletePopup(user.id)" v-if="user.login !== 'admin'">Удалить</button></td>
        </tr>        
        </div>`,
    created: function () {
        usersControllerGetAll.get().then(result => result.json().then(data => data.forEach(user => {
                this.listOfUsers.push(user)
            }
        )))
    },
    watch: {
        tableUpdateToggle: function () {
            usersApp.listOfUsers = [];
            usersControllerGetAll.get().then(result => result.json().then(data => data.forEach(user => {
                    this.listOfUsers.push(user)
                }
            )))
        }
    },
    methods: {
        showUpdatePopup(userId) {
            usersApp.showUpdatePopup(userId)
        },
        showDeletePopup(userId) {
            usersApp.showDeletePopup(userId)
        }
    }

});

let positionsForAdd = Vue.component('app-positions-for-add', {
    props: {
        positionsErrorEnable: false,
    },
    data() {
        return {
            listOfPositions: [],
            position: 'Выберите должность'
        }
    },
    template: `
<div>
<select v-model="position" @change="assignPositionTonewObject" class="form_input" :class="{\'input-error-style\': positionsErrorEnable}" id="positionsselector" >
      <option :value="position" disabled selected>{{position}}</option>
      <option v-for="position in listOfPositions" v-bind:value="position.position">{{position.position}}</option>
        </select>
</div>`,
    created: function () {
        positionsControllerApi.get().then(result => result.json().then(data => data.forEach(position => {
                this.listOfPositions.push(position)
            }
        )))
    },
    methods: {
        assignPositionTonewObject() {
            usersApp.newObject.position.position = this.position;
        }
    }
});

let positionsForUpdate = Vue.component('app-positions-for-update', {
    props: {
        selectedPosition:""
    },
    data() {
        return {
            listOfPositions: [],
            position: ''
        }
    },
    template: `<div v-if="selectedPosition === 'admin'">
        <select v-model="position" class="form_input" disabled>
      <option :value="selectedPosition" selected>{{selectedPosition}}</option>
        </select>        
      </div>
<div v-else> 
<select v-model="position" @change="assignNewPosition" class="form_input" id="positionsselector" >      
       <option v-for="position in listOfPositions" v-bind:value="position.position" > {{position.position}}</option>
       </select>
</div>`,
    created: function () {
        positionsControllerApi.get().then(result => result.json().then(data => data.forEach(position => {
                this.listOfPositions.push(position)
            }
        )))
    },
    mounted() {
        this.position = this.selectedPosition //присваиваем селектору первоначальное значение, которое присвоено пользователю
    },
    methods: {
        assignNewPosition() {
            usersApp.newObject.position.position = this.position;
        }
    }
});



var usersApp = new Vue({
    el: '#users-app',
    data: function () {
        return {
            listOfUsers: [],
            listOfPositions: [],
            isAddPopupVisible: false,
            isUpdatePopupVisible: false,
            isDeletePopupVisible:false,
            newObject: {},

            editableLoginField: false,
            error_message: '',
            loginErrorEnable: false,
            passErrorEnable: false,
            repassErrorEnable: false,
            surnameErrorEnable: false,
            nameErrorEnable: false,
            positionsErrorEnable: false,
            toggleForUpdate: true,
        }
    },
    methods: {
        showAddPopup() {
            this.resetNewObject();
            this.isAddPopupVisible = true;
        },
        showUpdatePopup(userId) {
            this.initObjectWithFieldsForUpdateOrDelete(userId);
            this.checkIfCanEditLoginField();
            this.isUpdatePopupVisible = true;
        },
        initObjectWithFieldsForUpdateOrDelete(userId) {
            var user = this.findUserById(userId);
            this.newObject = _.cloneDeep(user); //клонируем объект методом библиотеки lodash
            this.newObject.password = "";
        },
        showDeletePopup(userId) {
            this.initObjectWithFieldsForUpdateOrDelete(userId);
            this.isDeletePopupVisible = true;
        },
        closeAddPopupWindow() {
            this.resetErrorStyles();
            this.isAddPopupVisible = false;
        },
        closeUpdatePopupWindow() {
            this.resetErrorStyles();
            this.isUpdatePopupVisible = false;
        },
        closeDeletePopupWindow() {
            this.resetErrorStyles();
            this.isDeletePopupVisible = false;
        },
        validateAdd() {
            let result = true;
            this.resetErrorStyles();
            if (!this.checkLength(this.newObject.login, 3, 20)) {
                this.error_message = "Логин - обязательное поле, от 3 до 20 символов";
                this.loginErrorEnable = true;
                result = false;
            } else if (!this.checkLength(this.newObject.password, 5, 20)) {
                this.error_message = "Пароль - обязательное поле, от 5 до 20 символов";
                this.passErrorEnable = true;
                result = false;
            } else if (this.newObject.password !== this.newObject.repassword) {
                this.error_message = "Пароли не совпадают";
                this.repassErrorEnable = true;
                result = false;
            } else if (!this.checkLength(this.newObject.surname, 2, 25)) {
                this.error_message = "Фамилия - обязательное поле, от 2 до 25 символов";
                this.surnameErrorEnable = true;
                result = false;
            } else if (!this.checkLength(this.newObject.name, 2, 25)) {
                this.error_message = "Имя - обязательное поле, от 2 до 25 символов";
                this.nameErrorEnable = true;
                result = false;
            } else if (this.newObject.position.position === "") {
                this.error_message = "Нужно выбрать должность";
                this.positionsErrorEnable = true;
                result = false;
            }
            return result;
        },
        validateUpdate() {
            let result = true;
            this.resetErrorStyles();
            if (this.newObject.login.length > 0 && !this.checkLength(this.newObject.login, 3, 20)) {
                this.error_message = "Логин должен быть от 3 до 20 символов, или оставьте это поле пустым";
                this.loginErrorEnable = true;
                result = false;
            } else if (this.newObject.password.length > 0) {
                if (!this.checkLength(this.newObject.password, 5, 20)) {
                    this.error_message = "Пароль должен быть от 5 до 20 символов, или оставьте это поле пустым";
                    this.passErrorEnable = true;
                    result = false;
                } else if (this.newObject.password !== this.newObject.repassword) {
                    this.error_message = "Пароли не совпадают";
                    this.repassErrorEnable = true;
                    result = false;
                }
            } else if (this.newObject.surname.length > 0 && !this.checkLength(this.newObject.surname, 2, 25)) {
                this.error_message = "Фамилия должна быть от 2 до 25 символов, или оставьте это поле пустым";
                this.surnameErrorEnable = true;
                result = false;
            } else if (this.newObject.name.length > 0 && !this.checkLength(this.newObject.name, 2, 25)) {
                this.error_message = "Имя должно быть от 2 до 25 символов, или оставьте это поле пустым";
                this.nameErrorEnable = true;
                result = false;
            }
            return result;
        },

        checkLength(string, minLength, maxLength) {
            let result = true;
            if (string == null) { //проверка на undefined
                result = false;
            } else if (string.length < minLength || string.length > maxLength) {
                result = false;
            }
            return result;
        },
        resetErrorStyles() {
            this.error_message = '';
            this.loginErrorEnable = false;
            this.passErrorEnable = false;
            this.repassErrorEnable = false;
            this.surnameErrorEnable = false;
            this.nameErrorEnable = false;
            this.positionsErrorEnable = false;
        },
        resetNewObject() {
            this.newObject = {
                login:"",
                password:"",
                repassword:"",
                surname:"",
                name:"",
                position:{
                    position:""
                },
                active: true
            };
        },
        findUserById(id) {
            var foundUser;
            for (var i in this.listOfUsers) {
                if (this.listOfUsers[i].id === id) {
                    foundUser = this.listOfUsers[i];
                    break;
                }
            }
            return foundUser;
        },
        sendData() {
            usersController.save(this.newObject)
                .then(result => result.json().then(data => {
                    let addResult = data.addResult;
                    if (addResult === 'OK') {
                        this.toggleForUpdate = !this.toggleForUpdate;
                        this.closeAddPopupWindow();
                    } else {
                        this.error_message = addResult;
                    }
                }));
        },
        sendDataForUpdate() {
            delete this.newObject.repassword; //удаляем лишнее поле

            usersController.update(this.newObject)
                .then(result => result.json().then(data => {
                    let updateResult = data.updateResult;
                    if (updateResult === 'OK') {
                        this.toggleForUpdate = !this.toggleForUpdate;
                        this.closeUpdatePopupWindow();
                    } else {
                        this.error_message = updateResult;
                    }
                }));
        },
        validateAndAdd() {
            let validateResult = this.validateAdd();
            if (validateResult) {
                this.sendData();
            }
        },
        validateAndUpdate() {
            let validateResult = this.validateUpdate();
            if (validateResult) {
                this.sendDataForUpdate();
            }
        },
        sendDataForDelete() {
            usersController.delete({id: this.newObject.id})
                .then(result => result.json().then(data => {
                    let deleteResult = data.deleteResult;
                    if (deleteResult === 'OK') {
                        this.toggleForUpdate = !this.toggleForUpdate;
                        this.closeDeletePopupWindow();
                    } else {
                        this.error_message = deleteResult;
                    }
                }));
        },
        checkIfCanEditLoginField() {
            this.editableLoginField = this.newObject.login === 'admin';
        }
    },
    computed: {
        countOfRows() {
            return this.listOfUsers.length
        },
    },
});