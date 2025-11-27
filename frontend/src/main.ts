import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import PrimeVue from 'primevue/config'
import ConfirmationService from 'primevue/confirmationservice'
import ToastService from 'primevue/toastservice'

import '@/assets/main.css'
import 'primevue/resources/themes/md-light-indigo/theme.css'
import 'primeicons/primeicons.css'

const app = createApp(App)

app.use(createPinia())
app.use(PrimeVue)
app.use(ConfirmationService)
app.use(ToastService)
app.use(router)

app.mount('#app')
