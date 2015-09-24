package controllers.advert

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import services.advert.AdvertServiceComponent
import domain.advert.Advert

trait AdvertController extends Controller {
    self: AdvertServiceComponent =>
    
   /* implicit val advertReads = (__ \ "email").read[String]
                                           .map(resource => AdvertResource(resource)) */

   /* implicit val advertReads = ( (__ \ "email").read[String] and (__ \ "fuel").read[String]
                                           ).map(resource => AdvertResource(resource)) */

   /* implicit val advertReads = (__ \ "email").read[String]
                                           .map(resource => AdvertResource(resource)) and
                              (__ \ "fuel").read[String]
                                           .map(resource => AdvertResource(resource))  */

    /* implicit val advertReads : Reads[Advert] = (
                              (__ \ "email").read[String]
                                           and
                              (__ \ "fuel").read[String] )
                                           (Advert.apply _)     */    
   // implicit val advertReads = (__ \ "email", __ \ "fuel").read[String]
   //                                        .map(resource => AdvertResource(resource))
    
   // val email = (json \ "email").as[String]
   // println(" email : " + email);
   // val fuel = (json \ "fuel").as[String]
   // println(" fuel : " + fuel);

  // implicit val advertReads = (__ \ "email").read[String]
  //                                         .map(resource => AdvertResource(resource))
    
  //  implicit val advertReads2 = (__ \ "fuel").read[String]
  //                                         .map(resource => AdvertResource(resource))


  implicit val advertReads = (__ \ "title").read[String]
                                           .map(resource => AdvertResource(resource))


    // za wczenie println("  advert.title : " + advert.title)
     
    
    implicit val advertWrites = new Writes[Advert] {
        override def writes(advert: Advert): JsValue = {
            Json.obj(
                "id" -> advert.id,
                "title" -> advert.title
                //"fuel"  -> advert.title
            )
        }
    }
                                           
    def createAdvert = Action(parse.json) {request =>
        unmarshalAdvertResource(request, (resource: AdvertResource) => {
            val advert = Advert(Option.empty,
                            resource.title 
                            //resource.fuel
                            )

            advertService.createAdvert(advert)
            println("  POST done ")
            Created
        })
    }
    
    def updateAdvert(id: Long) = Action(parse.json) {request =>
        unmarshalAdvertResource(request, (resource: AdvertResource) => {
            val advert = Advert(Option(id),
                            resource.title
                            //resource.fuel
                            )
            advertService.updateAdvert(advert)
            NoContent
        })
    }
    
    def findAdvertById(id: Long) = Action {
        val advert = advertService.tryFindById(id)
        if (advert.isDefined) {
            Ok(Json.toJson(advert))
        } else {
            NotFound
        }
    }
    
    def deleteAdvert(id: Long) = Action {
        advertService.delete(id)
        NoContent
    }
    
    private def unmarshalAdvertResource(request: Request[JsValue],
                                      block: (AdvertResource) => Result): Result = {
        request.body.validate[AdvertResource].fold(
            valid = block,
            invalid = (e => {
                val error = e.mkString
                Logger.error(error)
                BadRequest(error)
            })
        )
    }

}

case class AdvertResource (val title: String) //, val fuel: String)
