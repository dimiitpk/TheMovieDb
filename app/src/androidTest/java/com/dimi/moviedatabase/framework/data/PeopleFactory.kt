package com.dimi.moviedatabase.framework.data

import com.dimi.moviedatabase.business.domain.model.Person
import javax.inject.Inject

class PeopleFactory @Inject constructor() {

    fun createPerson(id: Long): Person {
        return Person(
            id = id,
            name = "Name",
            popular = 0.0,
            character = "Unknown",
            priority = id.toInt()
        )
    }

    fun createPeopleList(size: Int): List<Person> {
        val list = ArrayList<Person>()
        for (i in 0..size) {
            list.add(createPerson(i.toLong()))
        }
        return list
    }

}