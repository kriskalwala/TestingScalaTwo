package services.advert

import domain.advert.Advert
import repositories.advert.AdvertRepositoryComponent

trait AdvertServiceComponent {
    
    val advertService: AdvertService
    
    trait AdvertService {
        
        def createAdvert(advert: Advert): Advert
        
        def updateAdvert(advert: Advert)
        
        def tryFindById(id: Long): Option[Advert]
        
        def delete(id: Long)
    
    }

}

trait AdvertServiceComponentImpl extends AdvertServiceComponent {
    self: AdvertRepositoryComponent =>
    
    override val advertService = new AdvertServiceImpl
    
    class AdvertServiceImpl extends AdvertService {
        
        override def createAdvert(advert: Advert): Advert = {
            println(" landing in ADVERT REPOSITORY ")
            advertRepository.createAdvert(advert)
        }
        
        override def updateAdvert(advert: Advert) {
            advertRepository.updateAdvert(advert)
        }
        
        override def tryFindById(id: Long): Option[Advert] = {
            advertRepository.tryFindById(id)
        }
        
        override def delete(id: Long) {
            advertRepository.delete(id)
        }
        
    }
}
