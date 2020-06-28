let positionsControllerGetAll = Vue.resource('./positions_controller/all{/id}');
let positionsControllerGetFiltered = Vue.resource('./positions_controller/local_filter');
let positionsController = Vue.resource('./positions_controller{/id}');


Vue.component('app-positions', {
    props: [
        'listOfPositions',
        'tableUpdateToggle'
    ],
    template: `<div>
        <tr class="main_table_tr" v-for="position in listOfPositions">
        <td class="main_table_td column_1">{{position.position}}</td>
        <td class="main_table_td column_2">{{position.positionDescription}}</td> 
        <td class="main_table_td column_7"><button @click="showUpdatePopup(position.id)">Изменить</button></td>
        <td class="main_table_td column_8"><button @click="showDeletePopup(position.id)" >Удалить</button></td>
        </tr>        
        </div>`,
    created: function () {
        positionsControllerGetAll.get().then(result => result.json().then(data => data.forEach(position => {
                this.listOfPositions.push(position)
            }
        )))
    },
    watch: {
        tableUpdateToggle: function () {
            positionsApp.listOfPositions = [];
            positionsControllerGetAll.get().then(result => result.json().then(data => data.forEach(position => {
                    this.listOfPositions.push(position)
                }
            )))
        }
    },
    methods: {
        showUpdatePopup(positionId) {
            positionsApp.showUpdatePopup(positionId)
        },
        showDeletePopup(positionId) {
            positionsApp.showDeletePopup(positionId)
        }
    }

});







var positionsApp = new Vue({
    el: '#positions-app',
    data: function () {
        return {
            listOfPositions: [],
            isAddPopupVisible: false,
            isUpdatePopupVisible: false,
            isDeletePopupVisible:false,
            newObject: {},
            error_message: '',
            positionErrorEnable: false,
            descriptionErrorEnable: false,
            toggleForUpdate: true,
            position_input_for_update: '',
            description_input_for_update: '',
        }
    },
    methods: {
        showAddPopup() {
            this.isAddPopupVisible = true;
            this.resetNewObject();
        },
        showUpdatePopup(positionId) {
            this.resetNewObject();
            this.initObjectWithFieldsForUpdateOrDelete(positionId);
            this.isUpdatePopupVisible = true;
        },
        initObjectWithFieldsForUpdateOrDelete(positionId) {
            var position = this.findPositionById(positionId);
            this.newObject = _.cloneDeep(position); //клонируем объект методом библиотеки lodash
        },
        showDeletePopup(positionId) {
            this.initObjectWithFieldsForUpdateOrDelete(positionId);
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
            if (!this.checkLength(this.newObject.position, 3, 60)) {
                this.error_message = "Должность - обязательное поле, от 3 до 60 символов";
                this.positionErrorEnable = true;
                result = false;
            } else if (!this.checkLength(this.newObject.positionDescription, 3, 255)) {
                this.error_message = "Описание должности - обязательное поле, от 3 до 255 символов";
                this.descriptionErrorEnable = true;
                result = false;
            }
            return result;
        },
        validateUpdate() {
            console.log(this.newObject)
            let result = true;
            this.resetErrorStyles();
            if (this.newObject.position.length > 0 && !this.checkLength(this.newObject.position, 3, 60)) {
                this.error_message = "Должность - обязательное поле, от 3 до 60 символов, или оставьте это поле пустым";
                this.positionErrorEnable = true;
                result = false;
            } else if (this.newObject.positionDescription.length > 0 && !this.checkLength(this.newObject.positionDescription, 3, 255)) {
                this.error_message = "Описание должности - обязательное поле, от 3 до 255 символов, или оставьте это поле пустым";
                this.descriptionErrorEnable = true;
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
            this.positionErrorEnable = false;
            this.descriptionErrorEnable = false;
        },
        resetNewObject() {
            this.newObject = {
                position:"",
                positionDescription:"",
                permissions:[]
            };
        },
        findPositionById(id) {
            var foundPosition;
            for (var i in this.listOfPositions) {
                if (this.listOfPositions[i].id === id) {
                    foundPosition = this.listOfPositions[i];
                    break;
                }
            }
            return foundPosition;
        },
        sendData() {
            positionsController.save(this.newObject)
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
            positionsController.update(this.newObject)
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
            positionsController.delete({id: this.newObject.id})
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
    },
    computed: {
        countOfRows() {
            return this.listOfPositions.length
        },
    },
    components: {



    },
})