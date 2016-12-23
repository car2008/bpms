// Place your Spring DSL code here
import com.capitalbiotech.bpms.SendEmailAsynchronously
beans = {
	taskExecutor(org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor){
		corePoolSize = 10
		maxPoolSize = 30
		queueCapacity = 6
		keepAliveSeconds = 2000
	}
	mail(SendEmailAsynchronously) {
		taskExecutor = ref('taskExecutor')
	}
}
