package repositories.advert

import domain.advert.Advert
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

trait AdvertRepositoryComponent {
    val advertRepository: AdvertRepository
    
    trait AdvertRepository {
        
        def createAdvert(advert: Advert): Advert
        
        def updateAdvert(advert: Advert)
        
        def tryFindById(id: Long): Option[Advert]
        
        def delete(id: Long)
        
    }
}

trait AdvertRepositoryComponentImpl extends AdvertRepositoryComponent {
    override val advertRepository = new AdvertRepositoryImpl
    
    class AdvertRepositoryImpl extends AdvertRepository {
        
        val adverts = new ConcurrentHashMap[Long, Advert]
        val idSequence = new AtomicLong(0)
        
        override def createAdvert(advert: Advert): Advert = {
            val newId = idSequence.incrementAndGet()
            val createdAdvert = advert.copy(id = Option(newId))
            adverts.put(newId, createdAdvert)
            println(" created Advert: " + createdAdvert)
            createdAdvert
        }
        
        override def updateAdvert(advert: Advert) {
            adverts.put(advert.id.get, advert)
        }
        
        override def tryFindById(id: Long): Option[Advert] = {
            Option(adverts.get(id))
        }
        
        override def delete(id: Long) {
            adverts.remove(id)
        }
        
    }
    
}