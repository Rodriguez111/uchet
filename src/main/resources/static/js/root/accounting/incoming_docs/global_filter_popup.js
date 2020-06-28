var contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
let usersControllerGetAll = Vue.resource(contextPath + '/admin/users_controller/all');
let contractorsControllerGetAll = Vue.resource(contextPath + '/contractors_controller/all');
let docTypesControllerGetAll = Vue.resource(contextPath + '/incoming_doc_type_controller/all');

Vue.component('global-filter-popup', {
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
                    <span><i class="material-icons" @click="cancelPopup">close</i></span>
                </div>
                <div class="Dialog_body">
                    
          
                    
          <div class="global_filter_form">

            <h2 class="form_title">Глобальный фильтр выбора</h2>

            <div class="container_1">
                <div class="date_from_block">
                    <label class="form_label">Дата с:</label>
                    <input type="date" class="form_input" v-model="globalFilterParams.dateFrom">
                </div>
                <div class="date_to_block">
                    <label class="form_label">Дата по:</label>
                    <input type="date" class="form_input" v-model="globalFilterParams.dateTo">
                </div>
            </div>

            <div class="container_2">
                <div class="doc_number_block">
                    <label class="form_label">№ документа:</label>
                    <input type="text" class="form_input doc_number_input" @input="validateNumberInput" v-model="globalFilterParams.selectedNumber">
                    <br><br><br>
                </div>
                <div class="user_select_block">
                    <label class="form_label">Кто принял:</label>
                    <app-owners-selector></app-owners-selector>

                </div>
            </div>

            <div class="container_3">
                <div class="contractor_block">
                <label class="form_label">Поставщик:</label>
                    <app-contractors-selector ></app-contractors-selector>                   
                    
                </div>
                <div class="doc_type_block">
                     <label class="form_label">Тип документа:</label>
                     <app-doctypes-selector></app-doctypes-selector> 
                </div>
            </div>

            <div class="checkbox_and_error_info_block">

                <div class="info_block">
                    <p v-if="error_message !== ''">{{error_message}}</p>
                </div>
            </div>
        </div>
                    
                          
                    
                    
                </div>
                <div class="Dialog_footer">
                    <div class="modal_button_1_container">
                        <button class="modal_button_1" @click="okPopup">{{button_1_name}}</button>
                    </div>
                    <div class="modal_button_2_container">
                        <button class="modal_button_2" @click="cancelPopup">{{button_2_name}}</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>`,
    data() {
        return {
            globalFilterParams: {
                dateFrom: new Date().toISOString().slice(0, 10),
                dateTo: new Date().toISOString().slice(0, 10),
                selectedNumber: '',
                selectedOwners: [],
                selectedContractors: [],
                selectedDocTypes: []
            },
            error_message: ''
        }
    },
    methods: {
        validateNumberInput() {
            var result = true;
            this.error_message = "";
            if (this.globalFilterParams.selectedNumber.length > 10) {
                this.globalFilterParams.selectedNumber = this.globalFilterParams.selectedNumber.substring(0, 10);
                result = false;
            }
            if (!(/^[0-9]{0,10}$/).test(this.globalFilterParams.selectedNumber)) {
                this.error_message = "Допустимый формат ввода - 10 цифр";
                result = false;
            }
            return result;
        },
        cancelPopup() {
            this.$emit('cancel_popup')//посылаем событие наверх родительскому элементу

        },
        okPopup() {
            if (this.validateNumberInput()) {
                var paramsMap = this.handleGlobalFilterParams();
                this.$emit('apply_global_filter', paramsMap)//посылаем событие наверх родительскому элементу
            }
        },
        handleGlobalFilterParams() {
            var params = {
                _numeric_docNumber: '',
                _date_docDate: '',
                contractor: '',
                type: '',
                owner: '',
            };
            params.docNumber = this.globalFilterParams.selectedNumber;
            params._date_docDate = "" + this.globalFilterParams.dateFrom + this.globalFilterParams.dateTo;
            params._numeric_contractor$id = this.concatenateArrayToString(this.selectedContractors);
            params._numeric_type$id = this.concatenateArrayToString(this.selectedDocTypes);
            params._numeric_owner$id = this.concatenateArrayToString(this.selectedOwners);
            return params;
        },
        concatenateArrayToString(array) {
            var result = "";
            if (array != null) {
                for (var i = 0; i < array.length; i++) {
                    result += array[i] + "||";
                }
            }
            return result;
        },
    },
    created() {
        bus.$on('transmit_selected_owners', (owners) => {
            this.selectedOwners = owners;
        });
        bus.$on('transmit_selected_contractors', (contractors) => {
            this.selectedContractors = contractors;
        });
        bus.$on('transmit_selected_types', (types) => {
            this.selectedDocTypes = types;
        });
    },
    mounted() {
        var timeZoneOffset = (new Date()).getTimezoneOffset() * 60000; //offset in milliseconds
        var localISODate = (new Date(Date.now() - timeZoneOffset)).toISOString().slice(0, 10);
        this.globalFilterParams.dateFrom = this.globalFilterParams.dateTo = localISODate;
    }
});


Vue.component('app-owners-selector', {
    template: `<div class="user_selector">
                    <div v-for="owner in ownersList">
                        <input type="checkbox" :value="owner.id" v-model="selectedOwners"/>{{owner.fullName}} <br />                        
                    </div>
               </div>`,
    data() {
        return {
            ownersList: [],
            selectedOwners: []
        }
    },
    mounted() {
        usersControllerGetAll.get().then(result => result.json().then(data => data.forEach(user => {
                this.ownersList.push(user)
            }
        )));
    },
    watch: {
        selectedOwners() {
            bus.$emit('transmit_selected_owners', this.selectedOwners);

        }
    }
});

Vue.component('app-contractors-selector', {
    props: {
        header_title: '',

    },
    template: `<div class="contractor_selector">
                    <div v-for="contractor in contractorsList">
                        <input type="checkbox" :value="contractor.id" v-model="selectedContractors"/>{{contractor.name}} <br />                        
                    </div>
               </div>`,
    data() {
        return {
            contractorsList: [],
            selectedContractors: []
        }
    },
    mounted() {
        contractorsControllerGetAll.get().then(result => result.json().then(data => data.forEach(contractor => {
                this.contractorsList.push(contractor)
            }
        )));
    },
    watch: {
        selectedContractors() {
            bus.$emit('transmit_selected_contractors', this.selectedContractors);
        }
    }
});

Vue.component('app-doctypes-selector', {
    props: {
        header_title: '',

    },
    template: `<div class="doc_type_selector">
                    <div v-for="type in typesList">
                        <input type="checkbox" :value="type.id" v-model="selectedDocTypes"/>{{type.docType}} <br />                        
                    </div>
               </div>`,
    data() {
        return {
            typesList: [],
            selectedDocTypes: []
        }
    },
    mounted() {
        docTypesControllerGetAll.get().then(result => result.json().then(data => data.forEach(type => {
                this.typesList.push(type)
            }
        )));
    },
    watch: {
        selectedDocTypes() {
            bus.$emit('transmit_selected_types', this.selectedDocTypes);
        }
    }
});


var bus = new Vue({})

