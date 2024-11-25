import streamlit as st
import requests

st.header("Pagamento")

# Input for table number
table_number = st.number_input("Número da Mesa", min_value=1, step=1)

# Form to add payment details
with st.form(key='payment_form'):
    valor = st.number_input("Valor", min_value=0.0, step=0.01)
    tipoDePagamento = st.text_input("Tipo de Pagamento")
    payment = {"valor": valor, "mesa": table_number, "tipoDePagamento": tipoDePagamento}
    
    submit_payment = st.form_submit_button("Enviar pagamento")
    
    if submit_payment:
        response = requests.post("http://localhost:8080/camel/orders/payment", json=payment)
        st.write(response.text)
        st.success("Pagamento enviado com sucesso!")