package org.springframework.samples.petclinic.visit

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.assertj.core.api.Assertions
import org.junit.ClassRule
import org.junit.Test
import org.junit.rules.ExternalResource
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.samples.petclinic.owner.PetRepository
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.MySQLContainer
import java.sql.SQLException
import javax.sql.DataSource

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("mysql")
class VisitPetRepositoryContainersTest {

    @TestConfiguration
    class TestConfig {
        @Bean
        @Throws(SQLException::class)
        fun dataSource(): DataSource {
            var hikariConfig = HikariConfig()
            val portMapping = VisitPetRepositoryContainersTest.Companion.mysqlSQLContainer.getMappedPort(3306)
            hikariConfig.jdbcUrl = "jdbc:mysql://localhost:${portMapping}/test"
            hikariConfig.username = "root"
            hikariConfig.password = "test"
            hikariConfig.isAllowPoolSuspension = true

            val ds = HikariDataSource(hikariConfig);
            return ds
        }
    }


    companion object {
        var mysqlSQLContainer: KMysqlContainer = KMysqlContainer("mysql:5.7.8")

        @ClassRule
        @JvmField
        val resource: ExternalResource = object : ExternalResource() {


            override fun before() {
                println("ClassRule Before")
                this@Companion.mysqlSQLContainer.start()
            }


            override fun after() {
                println("ClassRule After")
            }
        }
    }



    class KMysqlContainer(imageName: String?) : MySQLContainer<KMysqlContainer>(imageName)


    @Autowired
    lateinit private var pets: PetRepository

    @Autowired
    lateinit private var visits: VisitRepository

    @Test
    @Throws(Exception::class)
    fun shouldFindVisitsByPetId() {
        val visits = this.visits.findByPetId(7)
        Assertions.assertThat(visits.size).isEqualTo(2)
        val visitArr = visits.toTypedArray()
        Assertions.assertThat(visitArr[0].date).isNotNull()
        Assertions.assertThat(visitArr[0].petId).isEqualTo(7)
    }

    @Test
    @Transactional
    open fun shouldAddNewVisitForPet() {
        var pet7 = this.pets.findById(7)
        val found = pet7.getVisits().size
        val visit = Visit()
        pet7.addVisit(visit)
        visit.description = "test"
        this.visits.save(visit)
        this.pets.save(pet7)

        pet7 = this.pets.findById(7)
        Assertions.assertThat(pet7.getVisits().size).isEqualTo(found + 1)
        Assertions.assertThat(visit.id).isNotNull()
    }
}

