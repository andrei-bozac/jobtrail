<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="esteEditare" value="${not empty contact.id}"/>
<c:set var="titluPagina" value="${esteEditare ? 'Editează contactul' : 'Contact nou'}" scope="request"/>
<c:set var="meniuActiv" value="companii" scope="request"/>
<%@ include file="/WEB-INF/views/fragmente/antet.jspf" %>

<div class="row justify-content-center">
    <div class="col-lg-8">

        <h1 class="h3 mb-1">${titluPagina}</h1>
        <p class="text-muted mb-3">
            Companie: <strong><c:out value="${companie.nume}"/></strong>
        </p>

        <c:if test="${not empty eroare}">
            <div class="alert alert-danger">
                <i class="bi bi-exclamation-triangle"></i> <c:out value="${eroare}"/>
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/contacte/salveaza" class="card shadow-sm">
            <div class="card-body">

                <input type="hidden" name="id" value="${contact.id}">
                <input type="hidden" name="companieId" value="${companie.id}">

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="nume" class="form-label">Nume <span class="text-danger">*</span></label>
                        <input type="text" id="nume" name="nume" class="form-control"
                               maxlength="100" required value="${fn:escapeXml(contact.nume)}">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="functie" class="form-label">Funcție</label>
                        <input type="text" id="functie" name="functie" class="form-control"
                               maxlength="100" placeholder="ex: Technical Recruiter"
                               value="${fn:escapeXml(contact.functie)}">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="text" id="email" name="email" class="form-control"
                               maxlength="150" placeholder="nume@firma.ro"
                               value="${fn:escapeXml(contact.email)}">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="telefon" class="form-label">Telefon</label>
                        <input type="text" id="telefon" name="telefon" class="form-control"
                               maxlength="30" value="${fn:escapeXml(contact.telefon)}">
                    </div>
                </div>

                <div class="mb-3">
                    <label for="linkedin" class="form-label">Profil LinkedIn</label>
                    <input type="text" id="linkedin" name="linkedin" class="form-control"
                           maxlength="255" placeholder="https://linkedin.com/in/..."
                           value="${fn:escapeXml(contact.linkedin)}">
                </div>

            </div>
            <div class="card-footer bg-white d-flex justify-content-end gap-2">
                <a href="${pageContext.request.contextPath}/companii/detalii?id=${companie.id}"
                   class="btn btn-outline-secondary">Anulează</a>
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-check-lg"></i> Salvează
                </button>
            </div>
        </form>

    </div>
</div>

<%@ include file="/WEB-INF/views/fragmente/subsol.jspf" %>