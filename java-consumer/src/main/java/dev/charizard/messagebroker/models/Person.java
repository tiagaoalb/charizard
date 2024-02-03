package dev.charizard.messagebroker.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity(name = "person")
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "age")
	private String age;

	@OneToMany(mappedBy = "person")
	private Set<Transaction> transactions;


	public Person() {
	}

	public Person(String id, String name, String age, Set<Transaction> transactions) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.transactions = transactions;
	}

	public static Person create(){
		//todo: domain logic
		return new Person();
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

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

}