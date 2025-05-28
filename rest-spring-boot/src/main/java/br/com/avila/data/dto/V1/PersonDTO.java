package br.com.avila.data.dto.V1;

//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//import java.util.Date;
//import com.fasterxml.jackson.annotation.JsonFilter;
//import com.fasterxml.jackson.annotation.JsonFormat;


import br.com.avila.model.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;

import java.util.List;
import java.util.Objects;

//@JsonPropertyOrder({"id","first_name", "lastName", "address","gender"})
//@JsonFilter("PersonFilter")
@Relation(value = "person", collectionRelation = "people")
public class PersonDTO extends RepresentationModel<PersonDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    //@JsonProperty("first_name")
    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;


    private String address;

  //  @JsonSerialize(using = GenderSerializer.class) //
    private String gender;

    private Boolean enabled;

    // @JsonInclude(JsonInclude.Include.NON_EMPTY)
    // private String phoneNumber;

    // @JsonFormat(pattern = "dd/MM/yyyy")
    // private Date birthDay;
   // private String sensitiveData;


    private String profileUrl;
    private String photoUrl;

    @JsonIgnore
    public List<Book> books;

    public PersonDTO() {}

    public PersonDTO(Long id, String firstName, String lastName, String address, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

 /*    public Date getBirthDay() {
        return birthDay;
    }

   public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSensitiveData() {
        return sensitiveData;
    }

    public void setSensitiveData(String sensitiveData) {
        this.sensitiveData = sensitiveData;
    }*/

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @JsonIgnore
    public String getName() {
        return (firstName != null ? firstName : "") + (lastName != null ? " " + lastName : "");
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PersonDTO personDTO = (PersonDTO) o;
        return Objects.equals(id, personDTO.id) && Objects.equals(firstName, personDTO.firstName) && Objects.equals(lastName, personDTO.lastName) && Objects.equals(address, personDTO.address) && Objects.equals(gender, personDTO.gender) && Objects.equals(enabled, personDTO.enabled) && Objects.equals(profileUrl, personDTO.profileUrl) && Objects.equals(photoUrl, personDTO.photoUrl) && Objects.equals(books, personDTO.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, firstName, lastName, address, gender, enabled, profileUrl, photoUrl, books);
    }
}
