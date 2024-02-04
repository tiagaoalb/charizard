package dev.charizard.messagebroker.models;

import dev.charizard.messagebroker.exceptions.EntityValidationException;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@Entity(name = "person")
public class Person {
	@Id
	@Column(name = "id", length = 16)
	private String id; //cpf or cnpj
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
			throw new EntityValidationException(errors);
		}
		return person;
	}

	private Set<String> validate() {
		var errors = new HashSet<String>();
		if (id.length() != 11 && id.length() != 14) { //cpf or cnpj
			errors.add("Invalid document:" + id);
		}
		if (name == null || name.isBlank()) {
			errors.add("Invalid name:" + name);
		}
		if (age == null || age <= 0 || age >= 150) {
			errors.add("Invalid age:" + age);
		}
		return errors;
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