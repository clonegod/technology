package week07.homework

trait TeaOrdByAgeAsc extends Ordering[CollegePerson] {
	override def compare(p1: CollegePerson, p2: CollegePerson): Int = p1.age - p2.age
}

trait StuOrdByScoreDesc extends Ordering[CollegePerson] {
	override def compare(p1: CollegePerson, p2: CollegePerson): Int = {
			(p2.asInstanceOf[Student].score - p1.asInstanceOf[Student].score).toInt
	}   
}
