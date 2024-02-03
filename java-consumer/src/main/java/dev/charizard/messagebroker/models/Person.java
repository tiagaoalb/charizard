package dev.charizard.messagebroker.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "person")
public class Person {
	@Id
	@Column(name = "id")
	private String id; //todo: cpf validate || CNPJ

	@Column(name = "name")
	private String name;

	@Column(name = "age")
	private Integer age;

	@OneToMany(mappedBy = "person", fetch = FetchType.EAGER) //eager due need to load old transactions
	private Set<Transaction> transactions = new HashSet<>();


	public Person() {
	}

	public Person(String id, String name, Integer age, Set<Transaction> transactions) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.transactions = transactions;
	}

	public static Person create(String document, String name, Integer age) {
		var person = new Person(
						document,
						name,
						age,
						new HashSet<Transaction>() //if user still doesn't exists, it doesn't have transactions
		);
		var errors = person.validate();
		if (!errors.isEmpty()) {
			//todo: catch and send to DLQ
		}
		return person;
	}

	private Set<String> validate() {
		//todo: business validation
		return Set.of();
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	public void registerTransaction(Transaction transaction) {
		this.transactions.add(transaction);
	}
}